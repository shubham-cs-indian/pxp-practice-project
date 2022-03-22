package com.cs.core.runtime.interactor.usecase.variant;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.asset.services.CommonConstants;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.interactor.exception.variants.UserNotHaveCreatePermissionForVariant;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForOverviewTabStrategy;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.runtime.utils.threadPoolExecutor.ThreadPoolExecutorUtil;
import com.cs.core.util.VariantUtils;
import com.cs.utils.BaseEntityUtils;

@SuppressWarnings("unchecked")
@Component
public abstract class AbstractCreateInstanceVariant<P extends ICreateVariantModel, R extends IGetKlassInstanceModel>
    extends AbstractRuntimeInteractor<P, R> {
  
  @Autowired
  protected ISessionContext                             context;
  
  @Autowired
  protected PermissionUtils                             permissionUtils;
  
  @Autowired
  protected IGetConfigDetailsWithoutPermissionsStrategy getConfigDetailsWithoutPermissionsStrategy;
  
  @Autowired
  protected IGetConfigDetailsForOverviewTabStrategy     getConfigDetailsForOverviewTabStrategy;
  
  @Autowired
  protected VariantUtils                                variantUtils;
  
  @Autowired
  protected TransactionThreadData                       transactionThread;
  
  @Autowired
  protected RDBMSComponentUtils                         rdbmsComponentUtils;
  
  @Autowired
  protected VariantInstanceUtils                        variantInstanceUtils;
  
  @Autowired
  ThreadPoolExecutorUtil                                threadPoolTaskExecutorUtil;
  
  protected abstract BaseType getBaseType();
  
  protected abstract Long getCounter();
  
  protected abstract String getModuleEntityType();
  
  @Override
  public R executeInternal(P createVariantModel) throws Exception
  {
    String type = createVariantModel.getTypes()
        .get(0);
    createVariantModel.setType(type);
    Boolean canCreate = permissionUtils.isUserHasPermissionToCreate(type, "klass",
        getModuleEntityType());
    if (!canCreate) {
      throw new UserNotHaveCreatePermissionForVariant();
    }
    
    long parentInstanceIid = Long.parseLong(createVariantModel.getParentId());
    IBaseEntityDAO parentEntityDAO = this.rdbmsComponentUtils
        .getBaseEntityDAO(parentInstanceIid);
    
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    multiclassificationRequestModel.getKlassIds()
        .add(type);
    TransactionData transactionData = transactionThread.getTransactionData();
    multiclassificationRequestModel.setEndpointId(transactionData.getEndpointId());
    multiclassificationRequestModel.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
    multiclassificationRequestModel.setOrganizationId(transactionData.getOrganizationId());
    multiclassificationRequestModel.setParentKlassIds(BaseEntityUtils.getAllReferenceTypeFromBaseEntity(parentEntityDAO));
    multiclassificationRequestModel.setParentTaxonomyIds(BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(parentEntityDAO.getClassifiers()));
    multiclassificationRequestModel.setUserId(transactionData.getUserId());
    
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsWithoutPermissionsStrategy
        .execute(multiclassificationRequestModel);
    
    String variantNames = createVariantModel.getName();
    /* String existingLanguageCode = variantNames.substring(variantNames.substring(0, variantNames.lastIndexOf("_")).lastIndexOf("_") + 1);*/
    if (variantNames == null) {
      createVariantModel.setName(variantInstanceUtils.getNewVariantNameForCreate(configDetails.getReferencedKlasses(), type, getCounter(),
          rdbmsComponentUtils.getDataLanguage()));
    }
    /*else if ((parentEntityDAO.getBaseEntityDTO().getLocaleIds()).contains(existingLanguageCode)) {
      createVariantModel.setName(variantNames.replace(existingLanguageCode, rdbmsComponentUtils.getDataLanguage()));
    }
    else if (parentEntityDAO.getBaseEntityDTO().getChildLevel() > 1) {
      createVariantModel.setName("nested embedded " + variantNames + "_context_" + rdbmsComponentUtils.getDataLanguage());
    }
    else {
      createVariantModel.setName(variantNames + "_context_" + rdbmsComponentUtils.getDataLanguage());
    }*/
    IBaseEntityDTO createBaseEntity = variantInstanceUtils.createVariantBaseEntity(configDetails,
        createVariantModel, getBaseType());
    IGetKlassInstanceModel returnModel = prepareDataForResponse(createVariantModel,
        createBaseEntity);
    
    rdbmsComponentUtils.createNewRevision(rdbmsComponentUtils.getBaseEntityDTO(parentInstanceIid), configDetails.getNumberOfVersionsToMaintainForParent());
    rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(createBaseEntity.getBaseEntityIID(), IEventDTO.EventType.ELASTIC_UPDATE);
    
    return (R) returnModel;
  }
  
  protected IGetKlassInstanceModel prepareDataForResponse(P createVariantModel,
      IBaseEntityDTO baseEntityDTO) throws Exception
  {
    String variantInstanceId = createVariantModel.getVariantInstanceId();
    
    IBaseEntityDAO baseEntityDAOByID = rdbmsComponentUtils.getBaseEntityDAO(baseEntityDTO);
    
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    multiclassificationRequestModel.setTemplateId(createVariantModel.getTemplateId());
    multiclassificationRequestModel.getKlassIds()
        .add(createVariantModel.getType());
    multiclassificationRequestModel.setTypeId(createVariantModel.getTypeId());
    multiclassificationRequestModel.setTabId(createVariantModel.getTabId());
    
    TransactionData transactionData = transactionThread.getTransactionData();
    multiclassificationRequestModel.setEndpointId(transactionData.getEndpointId());
    multiclassificationRequestModel.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
    multiclassificationRequestModel.setOrganizationId(transactionData.getOrganizationId());
    multiclassificationRequestModel
    .setLanguageCodes(Arrays.asList(transactionData.getDataLanguage()));
    multiclassificationRequestModel.setShouldUseTagIdTagValueIdsMap(false);
    multiclassificationRequestModel.setUserId(transactionData.getUserId());
    
    IBaseEntityDTO parentEntityDTO = baseEntityDAOByID.getParent();
    if(parentEntityDTO != null && parentEntityDTO.getBaseEntityIID() != 0) {
      IBaseEntityDAO parentEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(parentEntityDTO);
      multiclassificationRequestModel.setParentKlassIds(BaseEntityUtils.getAllReferenceTypeFromBaseEntity(parentEntityDAO));
      multiclassificationRequestModel.setParentTaxonomyIds(BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(parentEntityDAO.getClassifiers()));
    }
    
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsForOverviewTabStrategy
        .execute(multiclassificationRequestModel);
    
    KlassInstanceUtils.getInstanceRequestStrategyModel(CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE,
        variantInstanceId, createVariantModel.getTemplateId(), configDetails, 0, 20,
        new ArrayList<>());
    
    
    IGetKlassInstanceCustomTabModel returnModel = variantInstanceUtils
        .executeGetKlassInstanceForOverviewTab(configDetails, baseEntityDAOByID);
    permissionUtils.manageKlassInstancePermissions(returnModel);
    returnModel.setGlobalPermission(configDetails.getReferencedPermissions()
        .getGlobalPermission());
    return returnModel;
  }
}
