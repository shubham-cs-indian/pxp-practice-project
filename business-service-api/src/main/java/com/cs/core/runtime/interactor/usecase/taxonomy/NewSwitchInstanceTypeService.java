package com.cs.core.runtime.interactor.usecase.taxonomy;

import com.cs.bds.config.usecase.taxonomy.ITypeSwitchInstance;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForRuntimeEntitiesResponseModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipInstanceModel;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.runtime.builder.BuilderFactory;
import com.cs.core.runtime.instancetree.GoldenRecordUtils;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.klassinstance.IAssetInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IPropertyInstance;
import com.cs.core.runtime.interactor.exception.permission.UserNotHavePermissionToAddKlass;
import com.cs.core.runtime.interactor.exception.permission.UserNotHavePermissionToAddTaxonomy;
import com.cs.core.runtime.interactor.exception.permission.UserNotHavePermissionToDeleteKlass;
import com.cs.core.runtime.interactor.exception.permission.UserNotHavePermissionToDeleteTaxonomy;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeRequestModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.templating.*;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForCustomTabStrategy;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForOverviewTabStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.util.ConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public abstract class NewSwitchInstanceTypeService<P extends IKlassInstanceTypeSwitchModel, R extends IGetKlassInstanceModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected ISessionContext                         context;
  
  @Autowired
  protected PermissionUtils                         permissionUtils;
  
  @Autowired
  protected IGetConfigDetailsForOverviewTabStrategy getConfigDetailsForOverviewTabStrategy;
  
  @Autowired
  protected IGetConfigDetailsForCustomTabStrategy   getConfigDetailsForCustomTabStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                     rdbmsComponentUtils;
  
  @Autowired
  protected ConfigUtil                              configUtil;
  
  @Autowired
  protected VariantInstanceUtils                    variantInstanceUtils;
  
  @Autowired
  protected ITypeSwitchInstance                     typeSwitchInstance;
  
  @Autowired
  protected GoldenRecordUtils                       goldenRecordUtils;
  
  protected abstract IGetConfigDetailsForCustomTabModel getMultiClassificationDetails(
      IConfigDetailsForSwitchTypeRequestModel idParameterModel) throws Exception;
  
  @SuppressWarnings("unchecked")
  @Override
  protected R executeInternal(P typeSwitchModel) throws Exception
  {
    String userId = context.getUserId();
    permissionCheckBeforeTypeSwitch(typeSwitchModel, userId);
    
    IConfigDetailsForSwitchTypeRequestModel multiClassificationRequestModel = typeSwitchInstance.execute(typeSwitchModel);

    IGetConfigDetailsModel configDetails = getConfigDetailsForResponse(typeSwitchModel, multiClassificationRequestModel);
    
    IBaseEntityDAO baseEntityDao = rdbmsComponentUtils.getEntityDAO(Long.parseLong(typeSwitchModel.getKlassInstanceId()));
    IBaseEntityDTO baseEntityDto = baseEntityDao.getBaseEntityDTO();

    IGetKlassInstanceModel returnModel = executeKlassInstance(configDetails, baseEntityDao, typeSwitchModel);
    
    returnModel.setConfigDetails(configDetails);
    permissionUtils.manageKlassInstancePermissions(returnModel);
    // create new revision after type switch
    rdbmsComponentUtils.createNewRevision(baseEntityDao.getBaseEntityDTO(), configDetails.getNumberOfVersionsToMaintain());

    goldenRecordUtils.initiateEvaluateGoldenRecordBucket(baseEntityDto);
    
    rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdateWithLoadRecords(baseEntityDto);
    return (R) returnModel;
  }
  
  protected IGetConfigDetailsModel getConfigDetailsForResponse(P typeSwitchModel,
      IConfigDetailsForSwitchTypeRequestModel multiClassificationRequestModel) throws Exception
  {
    switch (typeSwitchModel.getTabType()) {
      case CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE:
        Boolean isIsMinorTaxonomySwitch = typeSwitchModel.getIsMinorTaxonomySwitch() != null && typeSwitchModel.getIsMinorTaxonomySwitch();
        return getConfigDetails(multiClassificationRequestModel, isIsMinorTaxonomySwitch);
        
      default:
        return null;
    }
  }

  protected IGetConfigDetailsModel getConfigDetails(
      IConfigDetailsForSwitchTypeRequestModel multiClassificationRequestModel,
      Boolean isIsMinorTaxonomySwitch) throws Exception
  {
    if (isIsMinorTaxonomySwitch) {
      return getConfigDetailsForCustomTabStrategy.execute(multiClassificationRequestModel);
    }
    else {
      return getConfigDetailsForOverviewTabStrategy.execute(multiClassificationRequestModel);
    }
  }
  
  protected void permissionCheckBeforeTypeSwitch(P typeSwitchModel, String userId) throws Exception
  {
    
    List<String> addedTaxonomyIds = typeSwitchModel.getAddedTaxonomyIds();
    List<String> deletedTaxonomyIds = typeSwitchModel.getDeletedTaxonomyIds();
    List<String> addedKlassIds = typeSwitchModel.getAddedSecondaryTypes();
    List<String> deletedSecondaryTypes = typeSwitchModel.getDeletedSecondaryTypes();
    
    List<String> taxonomyIds = new ArrayList<>();
    taxonomyIds.addAll(addedTaxonomyIds);
    taxonomyIds.addAll(deletedTaxonomyIds);
    taxonomyIds.removeAll(Collections.singleton(null));
    
    List<String> klassIds = new ArrayList<>();
    klassIds.addAll(addedKlassIds);
    klassIds.addAll(deletedSecondaryTypes);
    klassIds.removeAll(Collections.singleton(null));
    
    IGetGlobalPermissionForRuntimeEntitiesResponseModel globalPermissionModel = permissionUtils
        .getGlobalPermissionsForRuntimeEntities(userId, taxonomyIds, klassIds);
    Map<String, IGlobalPermission> globalPermissions = globalPermissionModel.getGlobalPermissions();
    
    for (String addedTaxonomy : addedTaxonomyIds) {
      IGlobalPermission iGlobalPermission = globalPermissions.get(addedTaxonomy);
      if (!iGlobalPermission.getCanCreate()) {
        throw new UserNotHavePermissionToAddTaxonomy();
      }
    }
    
    if (deletedTaxonomyIds != null) {
      for (String deletedTaxonomyId : deletedTaxonomyIds) {
        IGlobalPermission iGlobalPermission = globalPermissions.get(deletedTaxonomyId);
        if (!iGlobalPermission.getCanDelete()) {
          throw new UserNotHavePermissionToDeleteTaxonomy();
        }
      }
    }
    
    for (String addedKlass : addedKlassIds) {
      IGlobalPermission iGlobalPermission = globalPermissions.get(addedKlass);
      if (!iGlobalPermission.getCanCreate()) {
        throw new UserNotHavePermissionToAddKlass();
      }
    }
    
    if (deletedSecondaryTypes != null) {
      for (String deletedKlassId : deletedSecondaryTypes) {
        IGlobalPermission iGlobalPermission = globalPermissions.get(deletedKlassId);
        if (!iGlobalPermission.getCanDelete()) {
          throw new UserNotHavePermissionToDeleteKlass();
        }
      }
    }
  }
  
  protected IGetKlassInstanceModel executeKlassInstance(IGetConfigDetailsModel configDetails,
      IBaseEntityDAO baseEntityDAO, IKlassInstanceTypeSwitchModel typeSwitchModel) throws Exception
  {
    IGetKlassInstanceCustomTabModel returnModel = new GetKlassInstanceForCustomTabModel();
    KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO, configDetails, rdbmsComponentUtils);
    IKlassInstance klassInstance = klassInstanceBuilder.getKlassInstance();
//    KlassInstanceBuilder.fillEntityExtensionInAssetCoverFlowAttribute((List<IContentAttributeInstance>)klassInstance.getAttributes(), baseEntityDAO.getBaseEntityDTO());
    
    if(klassInstance.getBaseType().equals(Constants.ASSET_INSTANCE_BASE_TYPE)) {
      fillAssetInformation(klassInstance, baseEntityDAO, baseEntityDAO.getBaseEntityDTO().getEntityExtension());
    }
    returnModel.setKlassInstance((IContentInstance) klassInstance);
    
    returnModel.setReferencedInstances(variantInstanceUtils.fillContextualData(configDetails, baseEntityDAO, klassInstance));    
    executeGetRelationshipInstance(returnModel, (IGetConfigDetailsForCustomTabModel) configDetails, baseEntityDAO);
    return returnModel;
  }
  
  protected void executeGetRelationshipInstance(IGetKlassInstanceCustomTabModel returnModel,
      IGetConfigDetailsForCustomTabModel configDetails, IBaseEntityDAO baseEntityDAO)
      throws Exception
  {
    IRelationshipInstanceModel relationshipInstanceModel = BuilderFactory
        .newRelationshipInstanceBuilder(rdbmsComponentUtils, configDetails)
        .baseEntityDAO(baseEntityDAO)
        .build();
    
    returnModel.setContentRelationships(relationshipInstanceModel.getContentRelationships());
    returnModel.setReferenceRelationshipInstanceElements(
        relationshipInstanceModel.getReferenceRelationshipInstanceElements());
    returnModel.setNatureRelationships(relationshipInstanceModel.getNatureRelationships());
    returnModel.setReferenceNatureRelationshipInstanceElements(
        relationshipInstanceModel.getReferenceNatureRelationshipInstanceElements());
  }
  
  protected void fillAssetInformation(IKlassInstance klassInstance, IBaseEntityDAO baseEntityDAO,
      IJSONContent entityExtension) throws Exception
  {
    entityExtension.deleteField(IEventInstanceSchedule.START_TIME);
    entityExtension.deleteField(IEventInstanceSchedule.END_TIME);
    entityExtension.deleteField(IPropertyInstance.BASE_TYPE);
    
    fillEventScheduleData(klassInstance, baseEntityDAO);
    
    String entityExtensionAsString = entityExtension.toString();
    IAssetInformationModel assetInformationModel = (IAssetInformationModel) ObjectMapperUtil
        .readValue(entityExtensionAsString, AssetInformationModel.class);
    assetInformationModel.setHash(baseEntityDAO.getBaseEntityDTO().getHashCode());
    ((IAssetInstance) klassInstance).setAssetInformation(assetInformationModel);
  }

  private void fillEventScheduleData(IKlassInstance klassInstance, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    // copied from asset GetAssetInstanceForOverviewTabService.fillEntityInformation()
    IAssetInstance assetInstance = (IAssetInstance) klassInstance;
    IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder.getKlassInstanceInformationModel(baseEntityDAO.getBaseEntityDTO(), rdbmsComponentUtils);
    IEventInstanceSchedule eventSchedule = klassInstanceInformationModel.getEventSchedule();
    ConfigurationDAO configDAO = ConfigurationDAO.instance();

    List<IPropertyDTO> propertyDTO = new ArrayList<>();
    propertyDTO.add(configDAO.getPropertyByCode(SystemLevelIds.START_DATE_ATTRIBUTE));
    propertyDTO.add(configDAO.getPropertyByCode(SystemLevelIds.END_DATE_ATTRIBUTE));

    Set<IPropertyRecordDTO> properties = rdbmsComponentUtils.getEntityPropertyRecords(baseEntityDAO.getBaseEntityDTO().getBaseEntityIID(),
        propertyDTO);

    for (IPropertyRecordDTO propertyInstance : properties) {
      IPropertyDTO property = propertyInstance.getProperty();
      IValueRecordDTO value = (IValueRecordDTO) propertyInstance;
      if ((property.getCode()).equals(SystemLevelIds.START_DATE_ATTRIBUTE)) {
        eventSchedule.setStartTime((long) value.getAsNumber());
      }
      else {
        eventSchedule.setEndTime((long) value.getAsNumber());
      }
    }
    assetInstance.setEventSchedule(klassInstanceInformationModel.getEventSchedule());
    
  }
}
