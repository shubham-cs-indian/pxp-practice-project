/*package com.cs.core.runtime.interactor.usecase.variant;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.interactor.exception.variants.UserNotHaveCreatePermissionForVariant;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.ITemplatePermissionForGetVariantInstancesModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeInfoModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeInfoModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.variants.ConfigDetailsForGetVariantInstancesInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.GetVariantInstancesInTableViewStrategyModel;
import com.cs.core.runtime.interactor.model.variants.IConfigDetailsForGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.model.variants.IConfigDetailsForGetVariantInstancesInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantForLimitedObjectRequestModel;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewRequestStrategyModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForGetVariantInstancesInTableViewStrategy;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.runtime.strategy.usecase.configdetails.IGetConfigDetailsForContextualDataTransferStrategy;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.runtime.strategy.utils.WorkflowUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.BaseEntityUtils;

@SuppressWarnings("unchecked")
@Component
public abstract class AbstractCreateInstanceVariantForLimitedObject<P extends ICreateVariantForLimitedObjectRequestModel, R extends IGetVariantInstancesInTableViewModel>
    extends AbstractRuntimeInteractor<P, R> {
  
  @Autowired
  protected ISessionContext                                            context;
  
  @Autowired
  protected IGetConfigDetailsWithoutPermissionsStrategy                getConfigDetailsWithoutPermissionsStrategy;
  
  @Autowired
  protected KlassInstanceUtils                                         klassInstanceUtils;
  
  @Autowired
  protected IGetConfigDetailsForGetVariantInstancesInTableViewStrategy getconfigDetailsForGetVariantInstancesInTableViewStrategy;
  
  @Autowired
  protected PermissionUtils                                            permissionUtils;
  
  @Autowired
  protected TransactionThreadData                                      transactionThread;
  
  @Autowired
  protected VariantInstanceUtils                                       variantInstanceUtils;
  
  @Autowired
  protected RDBMSComponentUtils                                        rdbmsComponentUtils;
  
  @Autowired
  protected IGetConfigDetailsForContextualDataTransferStrategy getConfigDetailsForContextualDataTransferStrategy;

  private static final String SERVICE_FOR_CDT = "CONTEXTUAL_DATA_TRANSFER_TASK";

  protected abstract String getModuleEntityType();
  
  protected abstract Long getCounter();
  
  protected abstract BaseType getBaseType();
  
  protected abstract String getStringBaseType();
  
  public WorkflowUtils.UseCases getUsecase()
  {
    return WorkflowUtils.UseCases.UNDEFINED;
  }

  @Override
  protected R executeInternal(P createVariantForLimitedObjectRequestModel) throws Exception
  {
    ICreateVariantModel createVariantModel = createVariantForLimitedObjectRequestModel
        .getCreateVariantRequest();
    
    String immediateParentId = null;
    String parentVariantInstanceId = createVariantModel.getParentId();
    if (parentVariantInstanceId != null) {
      immediateParentId = parentVariantInstanceId;
    }
    else {
      immediateParentId = createVariantModel.getId();
    }

    String type = createVariantModel.getTypes()
        .get(0);
    createVariantModel.setType(type);
    Boolean canCreate = permissionUtils.isUserHasPermissionToCreate(type, "klass",
        getModuleEntityType());
    if (!canCreate) {
      throw new UserNotHaveCreatePermissionForVariant();
    }
    IBaseEntityDAO parentEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(Long.parseLong(createVariantModel.getParentId()));

    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    multiclassificationRequestModel.getKlassIds()
        .add(type);
    TransactionData transactionData = transactionThread.getTransactionData();
    multiclassificationRequestModel.setEndpointId(transactionData.getEndpointId());
    multiclassificationRequestModel.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
    multiclassificationRequestModel.setOrganizationId(transactionData.getOrganizationId());
    multiclassificationRequestModel.setUserId(transactionData.getUserId());

    // Needed for contextual data propagation
     multiclassificationRequestModel.setParentKlassIds(BaseEntityUtils.getAllReferenceTypeFromBaseEntity(parentEntityDAO));
     multiclassificationRequestModel.setParentTaxonomyIds(BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(parentEntityDAO.getClassifiers()));
     multiclassificationRequestModel.setUserId(transactionData.getUserId());

    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsWithoutPermissionsStrategy
        .execute(multiclassificationRequestModel);

    if (createVariantModel.getName() == null) {
      createVariantModel.setName(variantInstanceUtils
          .getNewVariantNameForCreate(configDetails.getReferencedKlasses(), type, getCounter()));
    }
    IBaseEntityDTO varient =  variantInstanceUtils.createVariantBaseEntity(configDetails, createVariantModel, getBaseType());

    rdbmsComponentUtils.createNewRevision(parentEntityDAO.getBaseEntityDTO(), configDetails.getNumberOfVersionsToMaintainForParent());

    IGetVariantInstanceInTableViewRequestModel tableViewRequestModel = createVariantForLimitedObjectRequestModel
        .getTableViewRequest();

    initiateContextualDataTransfer(createVariantForLimitedObjectRequestModel, varient);

    rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(varient.getBaseEntityIID(), IEventDTO.EventType.ELASTIC_UPDATE);
    R response = prepareDataForResponse(tableViewRequestModel, createVariantModel.getBaseType());
    IIdParameterModel varientIdModel = new IdParameterModel(String.valueOf(varient.getBaseEntityIID()));
    workflowUtils.executeBusinessProcessEvent(getUsecase().actionType, createVariantModel, varientIdModel);
    return response;
  }

  private void initiateContextualDataTransfer(P createVariantForLimitedObjectRequestModel, IBaseEntityDTO varient)
      throws Exception, RDBMSException, CSFormatException
  {
     String contentID = createVariantForLimitedObjectRequestModel.getCreateVariantRequest().getParentId();
    
    IContextualDataTransferGranularDTO dataTransferDTO = new ContextualDataTransferGranularDTO();
    dataTransferDTO.setParentBaseEntityIID((Long.parseLong(contentID)));
    dataTransferDTO.setVariantBaseEntityIID(varient.getBaseEntityIID());
    dataTransferDTO.setContextID(createVariantForLimitedObjectRequestModel.getCreateVariantRequest().getTypes().get(0));
    
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.HIGH;
    dataTransferDTO.setLocaleID(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID());
    dataTransferDTO.setCatalogCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
    dataTransferDTO.setOrganizationCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getOrganizationCode());
    dataTransferDTO.setUserId(transactionThread.getTransactionData().getUserId());
    BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), SERVICE_FOR_CDT, "", userPriority,
        new JSONContent(dataTransferDTO.toJSON()));
  }

  protected IKlassInstanceTypeInfoModel getKlassInstanceType(IBaseEntityDAO baseEntityDAO)
      throws Exception
  {
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    IKlassInstanceTypeInfoModel klassInstanceTypeModel = new KlassInstanceTypeInfoModel();

    List<String> types = new ArrayList<>();
    types.add(baseEntityDTO.getNatureClassifier().getCode());
    klassInstanceTypeModel.setKlassIds(types);
    List<String> taxonomyIds = new ArrayList<>();;
    List<IClassifierDTO> classifiers = baseEntityDAO.getClassifiers();
    for(IClassifierDTO classifier : classifiers) {
      if(classifier.getClassifierType().equals(ClassifierType.CLASS)){
        types.add(classifier.getCode());
      }
      else {
        taxonomyIds.add(classifier.getCode());
      }
    }
    klassInstanceTypeModel.setTaxonomyIds(taxonomyIds);
    //klassInstanceTypeModel.setName(baseEntityDTO.getBaseEntityName());
    //klassInstanceTypeModel.setBaseType(IConfigMap.getBaseTypeClassPath(baseEntityDTO.getBaseType()));

    List<String> languageCodes = new ArrayList<>();
    languageCodes.add(baseEntityDTO.getBaseLocaleID());
    klassInstanceTypeModel.setLanguageCodes(languageCodes);

    return klassInstanceTypeModel;
  }

  private R prepareDataForResponse(IGetVariantInstanceInTableViewRequestModel requestModel,
      String baseType) throws Exception
  {
    IIdParameterModel idParameterModel = new IdParameterModel(requestModel.getContextId());
    idParameterModel.setCurrentUserId(context.getUserId());
    
    IConfigDetailsForGetVariantInstancesInTableViewRequestModel configRequestModel = new ConfigDetailsForGetVariantInstancesInTableViewRequestModel();
    configRequestModel.setUserId(context.getUserId());
    configRequestModel.setContextId(requestModel.getContextId());
    configRequestModel.setPropertyIds(requestModel.getColumnIds());
    ;
    configRequestModel.setBaseType(getStringBaseType());
    IConfigDetailsForGetVariantInstancesInTableViewModel configDetails = getconfigDetailsForGetVariantInstancesInTableViewStrategy
        .execute(configRequestModel);
    
    IGetVariantInstancesInTableViewRequestStrategyModel tableviewRequestModel = new GetVariantInstancesInTableViewStrategyModel();
    ITemplatePermissionForGetVariantInstancesModel referencedPermissions = (ITemplatePermissionForGetVariantInstancesModel) configDetails
        .getReferencedPermissions();
    
    tableviewRequestModel.setAttributes(requestModel.getAttributes());
    tableviewRequestModel.setKlassInstanceId(requestModel.getKlassInstanceId());
    tableviewRequestModel.setContextId(requestModel.getContextId());
    tableviewRequestModel.setFrom(requestModel.getFrom());
    tableviewRequestModel.setParentId(requestModel.getParentId());
    tableviewRequestModel.setReferencedElements(configDetails.getReferencedElements());
    tableviewRequestModel.setReferencedTags(configDetails.getReferencedTags());
    tableviewRequestModel.setReferencedVariantContexts(configDetails.getReferencedVariantContexts()
        .getEmbeddedVariantContexts());
    tableviewRequestModel.setSize(requestModel.getSize());
    tableviewRequestModel.setSortOptions(requestModel.getSortOptions());
    tableviewRequestModel.setTags(requestModel.getTags());
    tableviewRequestModel.setColumnIds(requestModel.getColumnIds());
    tableviewRequestModel.setCurrentUserId(context.getUserId());
    tableviewRequestModel.setFilterInfo(configDetails.getFilterInfo());
    tableviewRequestModel.setTimeRange(requestModel.getTimeRange());
    tableviewRequestModel.setEntities(new ArrayList<>(referencedPermissions.getEntitiesHavingRP()));
    tableviewRequestModel.setAllSearch(requestModel.getAllSearch());
    tableviewRequestModel.setKlassIdsHavingRP(referencedPermissions.getKlassIdsHavingRP());
    tableviewRequestModel
        .setContextKlassIdsHavingRP(referencedPermissions.getContextKlassIdsHavingRP());
    tableviewRequestModel
        .setReferencedPropertyCollections(configDetails.getReferencedPropertyCollections());
    IGetVariantInstancesInTableViewModel returnModel = variantInstanceUtils
        .executeGetVariantInstances(tableviewRequestModel, configDetails);
    variantInstanceUtils.skipAttributesHavingAttributeContexts(returnModel.getColumns(),
    configDetails.getReferencedElements());
    returnModel.setConfigDetails(configDetails);
    returnModel.setFilterInfo(configDetails.getFilterInfo());
    return (R) returnModel;
  }
  
   private IBaseEntityDTO prepareDataAndCreateVariant(ICreateVariantModel createVariantModel) throws Exception
  {
    String type = createVariantModel.getTypes()
        .get(0);
    createVariantModel.setType(type);
    Boolean canCreate = permissionUtils.isUserHasPermissionToCreate(type, "klass",
        getModuleEntityType());
    if (!canCreate) {
      throw new UserNotHaveCreatePermissionForVariant();
    }
    
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    multiclassificationRequestModel.getKlassIds()
        .add(type);
    TransactionData transactionData = transactionThread.getTransactionData();
    multiclassificationRequestModel.setEndpointId(transactionData.getEndpointId());
    multiclassificationRequestModel.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
    multiclassificationRequestModel.setOrganizationId(transactionData.getOrganizationId());
    multiclassificationRequestModel.setUserId(transactionData.getUserId());
    
    // Needed for contextual data propagation
     multiclassificationRequestModel.setParentKlassIds(BaseEntityUtils.getAllReferenceTypeFromBaseEntity(parentEntityDAO));
     multiclassificationRequestModel.setParentTaxonomyIds(BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(parentEntityDAO.getClassifiers()));
     multiclassificationRequestModel.setUserId(transactionData.getUserId());
    
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsWithoutPermissionsStrategy
        .execute(multiclassificationRequestModel);
    
    if (createVariantModel.getName() == null) {
      createVariantModel.setName(variantInstanceUtils
          .getNewVariantNameForCreate(configDetails.getReferencedKlasses(), type, getCounter()));
    }
    return variantInstanceUtils.createVariantBaseEntity(configDetails, createVariantModel, getBaseType());
  }
}
*/