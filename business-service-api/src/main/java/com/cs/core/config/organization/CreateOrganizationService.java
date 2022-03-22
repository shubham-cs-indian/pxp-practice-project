package com.cs.core.config.organization;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.organization.OrganizationNotFoundException;
import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.config.interactor.model.organization.GetOrganizationModel;
import com.cs.core.config.interactor.model.organization.IGetOrganizationModel;
import com.cs.core.config.interactor.model.organization.IOrganizationModel;
import com.cs.core.config.interactor.model.role.CreateRoleModel;
import com.cs.core.config.interactor.model.role.ICreateRoleModel;
import com.cs.core.config.interactor.model.role.IRoleSaveModel;
import com.cs.core.config.interactor.model.role.RoleSaveModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.interactor.model.user.UserModel;
import com.cs.core.config.strategy.usecase.organization.ICreateOrganizationStrategy;
import com.cs.core.config.strategy.usecase.role.ICreateRoleStrategy;
import com.cs.core.config.strategy.usecase.role.ISaveRoleStrategy;
import com.cs.core.config.strategy.usecase.user.ICreateUserStrategy;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.instance.CreateInstanceModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.strategy.utils.AuthUtils;
import com.cs.pim.runtime.supplierinstance.ICreateSupplierInstanceService;
import com.cs.utils.BaseEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CreateOrganizationService
    extends AbstractCreateConfigService<IOrganizationModel, IGetOrganizationModel>
    implements ICreateOrganizationService {
  
  public static final List<String>      partnerTypes = Arrays.asList(
      Constants.SUPPLIERS_ORGANIZATION, Constants.MARKETPLACES_ORGANIZATION,
      Constants.DISTRIBUTORS_ORGANIZATION, Constants.WHOLESALERS_ORGANIZATION,
      Constants.TRANSLATION_AGENCY_ORGANIZATION, Constants.CONTENT_ENRICHMENT_AGENCY_ORGANIZATION,
      Constants.DIGITAL_ASSET_AGENCY_ORGANIZATION);
  
  @Autowired
  protected ICreateOrganizationStrategy createOrganizationStrategy;
  
  @Autowired
  protected IGetOrganizationService getOrganization;
 
  
  @Autowired
  protected TransactionThreadData       transactionThread;
  

  @Autowired
  protected ICreateSupplierInstanceService createSupplierInstanceService;

  @Autowired
  protected ICreateRoleStrategy         createRoleStrategy;
  
  @Autowired
  protected ICreateUserStrategy         createUserStrategy;
  
  @Autowired
  protected ISaveRoleStrategy           saveRoleStrategy;

  @Override
  public IGetOrganizationModel executeInternal(IOrganizationModel model) throws Exception
  {
    IGetOrganizationModel returnModel = new GetOrganizationModel();
    try {
      if (model.getIsOnboardingRequest() == true) {
        IIdParameterModel getEntityModel = new IdParameterModel(model.getId());
        getOrganization.execute(getEntityModel);
      }
      else {
        returnModel = createOrganizationStrategy.execute(model);
      }
    }
    catch (OrganizationNotFoundException e) {
      returnModel = createOrganizationStrategy.execute(model);
    }
    createBackgroundUserAndRole(model, returnModel);
    createSupplierInstanceForOrganization(model, returnModel);
    return returnModel;
  }


  private void createSupplierInstanceForOrganization(IOrganizationModel model, IGetOrganizationModel returnModel) throws Exception
  {
    String organizationType = model.getType();
    if (partnerTypes.contains(organizationType)) {
      TransactionData transactionData = transactionThread.getTransactionData();
      // TODO : Need to find a better way.
      // Supplier instance always belongs to PIM catalog,
      // though Organization created in other catalogs.
      transactionData.setPhysicalCatalogId(Constants.PIM);

      ICreateInstanceModel klassInstance = new CreateInstanceModel();
      klassInstance.setId(returnModel.getOrganization().getId());
      klassInstance.setName(model.getLabel());
      klassInstance.setType(BaseEntityUtils.getSupplierClass(organizationType));
      klassInstance.setParentId("-1");
      klassInstance.setOrganizationId(transactionData.getOrganizationId());
      klassInstance.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
      klassInstance.setPortalId(transactionData.getPortalId());
      klassInstance.setEndpointId(transactionData.getEndpointId());
      if (transactionData.getEndpointId() == null) {
        klassInstance.setEndpointId("-1");
      }
      klassInstance.setOriginalInstanceId(model.getOriginalInstanceId());
      createSupplierInstanceService.execute(klassInstance);
      /*
      RMIG -16
      TODO: RMIG-16. Supplier instance  should be created when the implementation of rdms start
      createSupplierInstance.execute(klassInstance);
      */
      // TODO :: TEMP FIX FOR RELEASE
      /*Below kafka call is commented and instead of that, create supplier is called in main thread itself.
      Because of PRODUCT-4606. Supplier component shows RED because still supplier instance not created*/
      // kafkaUtils.prepareMessageData(klassInstance,
      // CreateSupplierInstance.class.getName());
    }
  }

  /**
   * @param model
   * @param returnModel
   * @throws Exception
   */
  private void createBackgroundUserAndRole(IOrganizationModel model,
      IGetOrganizationModel returnModel) throws Exception
  {
    //creating background user
    IUserModel userModel = createUserStrategy.execute(createUser(returnModel.getOrganization().getCode()));
    ICreateRoleModel createRoleModel = createRole(returnModel.getOrganization().getId(), returnModel.getOrganization().getCode());
   
    //creating background role
    createRoleStrategy.execute(createRoleModel);
    
    IRoleSaveModel roleSaveModel = new RoleSaveModel();
    List<String> addedUsers = new ArrayList<String>();
    addedUsers.add(userModel.getId());
    roleSaveModel.setId(createRoleModel.getId());
    roleSaveModel.setIsMultiselect(false);
    roleSaveModel.setIsBackgroundRole(createRoleModel.getIsBackgroundRole());
    roleSaveModel.setAddedUsers(addedUsers);
    setEmptyParameters(roleSaveModel);
    
    //updating the background role
    saveRoleStrategy.execute(roleSaveModel);
  }
  
  /**
   * 
   * @param organizationCode
   * @return userModel
   * @throws Exception
   */
  private IUserModel createUser(String organizationCode) throws Exception
  {
    IUserModel userModel = new UserModel();
    // preparing user Model to create background user
    String encodedPassword = AuthUtils.getSaltedHash(CommonConstants.BACKGROUND_USER_PASSWORD);
    userModel.setId(CommonConstants.BACKGROUND_USER_ID_PEFIX + organizationCode.toLowerCase());
    userModel.setCode(CommonConstants.BACKGROUND_USER_ID_PEFIX + organizationCode.toLowerCase());
    userModel.setPassword(encodedPassword);
    userModel.setUserName(CommonConstants.BACKGROUND_USER_PEFIX + organizationCode);
    userModel.setFirstName(organizationCode);
    userModel.setLastName(CommonConstants.BACKGROUND_USER_LAST_NAME);
    userModel.setEmail(CommonConstants.BACKGROUND_USER_EMAIL_ID);
    userModel.setType(Constants.USER_BASE_TYPE);
    userModel.setIsStandard(true);
    userModel.setIsBackgroundUser(true);
    return userModel;
  }
  /**
   * 
   * @param organizationId
   * @param organizationCode
   * @return roleModel
   * @throws Exception
   */
  private ICreateRoleModel createRole(String organizationId, String organizationCode)
      throws Exception
  {
    ICreateRoleModel roleModel = new CreateRoleModel();
    // preparing r Model to create background user
    roleModel.setRoleType(CommonConstants.USER);
    roleModel.setId(CommonConstants.BACKGROUND_ROLE_ID_PEFIX + organizationCode.toLowerCase());
    roleModel.setCode(CommonConstants.BACKGROUND_ROLE_ID_PEFIX + organizationCode.toLowerCase());
    roleModel.setLabel(CommonConstants.BACKGROUND_ROLE_PEFIX + organizationCode);
    roleModel.setType("com.cs.core.config.interactor.entity.standard.role.BackgroundRole");
    
    roleModel.setOrganizationId(organizationId);
    roleModel.setIsBackgroundRole(true);
    roleModel.setIsMultiselect(false);
    
    return roleModel;
  }
  
  /**
   * @param roleSaveModel
   */
  private void setEmptyParameters(IRoleSaveModel roleSaveModel)
  {
    roleSaveModel.setDeletedUsers(new ArrayList<>());
    
    roleSaveModel.setAddedEndpoints(new ArrayList<>());
    roleSaveModel.setDeletedEndpoints(new ArrayList<>());
    roleSaveModel.setEndpoints(new ArrayList<>());
    
    roleSaveModel.setAddedTargetKlasses(new ArrayList<>());
    roleSaveModel.setDeletedTargetKlasses(new ArrayList<>());
    roleSaveModel.setTargetKlasses(new ArrayList<>());
    
    roleSaveModel.setaddedSystemIds(new ArrayList<>());
    roleSaveModel.setDeletedSystemIds(new ArrayList<>());
    roleSaveModel.setSystems(new ArrayList<>());
    
    roleSaveModel.setAddedTargetTaxonomies(new ArrayList<>());
    roleSaveModel.setDeletedTargetTaxonomies(new ArrayList<>());
    roleSaveModel.setTargetTaxonomies(new ArrayList<>());
    
    roleSaveModel.setddedKPIs(new ArrayList<>());
    roleSaveModel.setddedKPIs(new ArrayList<>());
    roleSaveModel.setKpis(new ArrayList<>());
  }
}
