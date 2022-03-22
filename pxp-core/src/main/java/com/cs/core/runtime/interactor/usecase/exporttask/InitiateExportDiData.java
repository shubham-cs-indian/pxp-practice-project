package com.cs.core.runtime.interactor.usecase.exporttask;
//
//import com.cs.constants.CommonConstants;
//import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
//import com.cs.core.config.interactor.model.articleimportcomponent.IComponentModel;
//import com.cs.core.config.interactor.model.attributiontaxonomy.IBulkCreateTaxonomyResponseModel;
//import com.cs.core.config.interactor.model.attributiontaxonomy.IGetAttributionTaxonomyModel;
//import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
//import com.cs.core.config.interactor.model.processdetails.IGetAllSourceDestinationIdsModel;
//import com.cs.core.config.interactor.model.processdetails.IProcessStatusDetailsModel;
//import com.cs.core.config.interactor.model.processdetails.ProcessStatusDetailsModel;
//import com.cs.core.config.interactor.model.processevent.IGetProcessEndpointEventModel;
//import com.cs.core.config.interactor.model.processevent.IGetProcessExportEndpointModel;
//import com.cs.core.config.interactor.model.taxonomy.IBulkCreateLinkedTaxonomyResponseModel;
//import com.cs.core.config.interactor.model.taxonomy.IGetHierarchyTaxonomyModel;
//import com.cs.core.config.interactor.model.taxonomy.IGetHierarchyTaxonomyWithoutKPModel;
//import com.cs.core.config.interactor.model.transfer.ITransferDataRequestModel;
//import com.cs.core.rdbms.driver.RDBMSLogger;
//import com.cs.core.runtime.interactor.constants.application.Constants;
//import com.cs.core.runtime.interactor.constants.application.ProcessConstants;
//import com.cs.di.workflow.trigger.event.IBusinessProcessTriggerModel;
//import com.cs.core.runtime.interactor.model.camunda.CamundaBroadcastEventModel;
//import com.cs.core.runtime.interactor.model.camunda.ICamundaBroadcastEventModel;
//import com.cs.core.runtime.interactor.model.configuration.IModel;
//import com.cs.core.runtime.interactor.model.instance.GetInstanceRequestModel;
//import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
//import com.cs.core.runtime.interactor.model.transfer.IDiExportDataModel;
//import com.cs.core.runtime.interactor.usecase.articleinstance.IGetArticleInstanceForOverviewTab;
//import com.cs.core.runtime.strategy.cammunda.broadcast.IBroadcastEventStrategy;
//import com.cs.core.runtime.strategy.cammunda.broadcast.IExecuteProcessStrategy;
//import com.cs.core.runtime.strategy.usecase.dataintegration.IGetProcessEventByEndpointIdStrategy;
//import com.cs.core.runtime.strategy.usecase.transfer.processstatus.IGetProcessStatusByProcessInstanceIdStrategy;
//import com.cs.core.runtime.strategy.usecase.transfer.sourcedestination.IGetSourceDestinationIdsByEntityTypeStrategy;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component("initiateExportDiData")
//@SuppressWarnings("unchecked")
//public class InitiateExportDiData extends AbstractInitiateExportDiData
//    implements IInitiateExportDiData {
//
//  /*@Autowired
//  protected IGetEndpointIdsOfCustomTypeProcessForUserStrategy getEndpointIdsOfCustomTypeProcessForUserStrategy;*/
//
//  @Autowired
//  protected IGetProcessEventByEndpointIdStrategy         getProcessEventByEndpointIdStrategy;
//
//  @Autowired
//  protected IExecuteProcessStrategy                      executeProcessStrategy;
//
//  /*@Autowired
//  protected IManualTransfer                                   manualTransferNew;*/
//
//  @Value("${minutes.before.retries}")
//  private Integer                                        minutesBeforeRetries;
//
//  @Value("${number.of.retries}")
//  private Integer                                        numberOfRetries;
//
//  @Autowired
//  protected IBroadcastEventStrategy                      broadcastEventStrategy;
//
//  @Autowired
//  protected IGetProcessStatusByProcessInstanceIdStrategy getProcessStatusByProcessInstanceIdStrategy;
//
//  @Autowired
//  protected IGetSourceDestinationIdsByEntityTypeStrategy getSourceDestinationIdsByEntityTypeStrategy;
//
//  @Autowired
//  protected IGetArticleInstanceForOverviewTab            getArticleInstanceForOverviewTab;
//
//  /*@Autowired
//  protected IGetAssetInstanceForOverviewTab   getAssetInstanceForOverviewTab;*/
//
//  @Override
//  public IModel execute(IDiExportDataModel dataModel) throws Exception
//  {
//    IBusinessProcessTriggerModel.UseCases useCase = dataModel.getUseCase();
//    /*
//     * get endpointIds list, organization, systemId for current user, will return list of endpoints if endpoint contains physicalCatalogIdForJms in PHYSICAL_CATALOGS list
//     */
//
//    switch (useCase) {
//      case SAVEARTIKLE:
//      case SAVEASSET:
//        processAfterSaveEvent(dataModel);
//        break;
//      case SAVETAXONOMY:
//        broadcastSaveTaxonomy(dataModel);
//        break;
//      case CREATETAXONOMY:
//        broadcastCreateTaxonomy(dataModel);
//        break;
//      case SAVEATTRIBUTIONTAXONOMY:
//        broadcastSaveAttributionTaxonomy(dataModel);
//        break;
//      case CREATEATTRIBUTIONTAXONOMY:
//        broadcastCreateAttributionTaxonomy(dataModel);
//        break;
//      case CREATEBULKATTRIBUTIONTAXONOMY:
//        broadcastBulkCreateAttributionTaxonomy(dataModel);
//        break;
//      case CREATEBULKTAXONOMY:
//        broadcastBulkCreateTaxonomy(dataModel);
//        break;
//      case DELETETAXONOMYLEVEL:
//        broadcastDeleteTaxonomyLevel(dataModel);
//      case DELETEATTRIBUTIONTAXONOMYLEVEL:
//        broadcastDeleteAttributionTaxonomyLevel(dataModel);
//        break;
//      default:
//        break;
//    }
//
//    return null;
//  }
//
//  private void processAfterSaveEvent(IDiExportDataModel dataModel) throws Exception
//  {
//    /*
//      String physicalCatalogId = dataModel.getPhysicalCatalogId();
//      String instanceId = dataModel.getKlassInstanceId();
//      IKlassInstanceSaveModel klassInstanceSaveModel = (IKlassInstanceSaveModel) dataModel.getDataInstance();
//      String baseType = klassInstanceSaveModel.getBaseType();
//      if(klassInstanceSaveModel.getKlassInstanceId() != null) {
//        System.out.println("Embedded Variant " + instanceId + " should not be exported");
//        return; // since we are not transferring or exporting product data when variant is saved.
//      }
//
//      if (checkViolation(dataModel)) {
//        System.out.println("Rule violation for " + instanceId);
//        return; // since we are not transferring or exporting product data when rule is violated.
//      }
//      IGetEndpointIdsRequestModel idParameterModel = new GetEndpointsIdsRequestModel();
//      idParameterModel.setUserId(controllerThread.getTransactionData().getUserId());
//      idParameterModel.setPhysicalCatalogId(physicalCatalogId);
//
//      IGetProcessExportEndpointModel getProcessExportEndpointModel = getEndpointIdsOfCustomTypeProcessForUserStrategy.execute(idParameterModel);
//
//        for (String endpointId : getProcessExportEndpointModel.getEndpoints()) {
//          IGetProcessEndpointEventModel getProcessEndpointEventModel = getProcessEventByEndpointIdStrategy.execute(new IdParameterModel(endpointId));
//          String organizationId = getProcessExportEndpointModel.getOrganization();
//          String processDefinitionId = getProcessEndpointEventModel.getProcessDefinitionId();
//
//          Boolean isTransferSuccess = manualTransfer(instanceId, endpointId, organizationId, baseType);
//
//          if(!isTransferSuccess){
//            RDBMSLogger.instance().info("*********  TRANSFER TO DESTINATION FAILED !!");
//            continue;
//          }
//
//          Map<String, Object> eventParameters = prepareEventParameterMap(dataModel.getDataInstance(),
//              instanceId ,physicalCatalogId, getProcessExportEndpointModel, endpointId, getProcessEndpointEventModel);
//          ICamundaExecuteProcessModel camundaExecuteProcessModel = new CamundaExecuteProcessModel(processDefinitionId, instanceId, eventParameters);
//          executeProcessStrategy.execute(camundaExecuteProcessModel);
//        }
//    */
//  }
//
//  private void broadcastCreateTaxonomy(IDiExportDataModel dataModel) throws Exception
//  {
//    IGetHierarchyTaxonomyWithoutKPModel responseModel = (IGetHierarchyTaxonomyWithoutKPModel) dataModel
//        .getDataInstance();
//    Map<String, Object> eventParameters = fillEventParameters(responseModel.getEntity()
//        .getId());
//    eventParameters.put(ProcessConstants.ACTION, "created");
//    ICamundaBroadcastEventModel camundaBroadcastEventModel = new CamundaBroadcastEventModel(
//        "AfterCreateTaxonomy", eventParameters);
//    broadcastEventStrategy.execute(camundaBroadcastEventModel);
//  }
//
//  private void broadcastSaveTaxonomy(IDiExportDataModel dataModel) throws Exception
//  {
//    IGetHierarchyTaxonomyWithoutKPModel responseModel = (IGetHierarchyTaxonomyWithoutKPModel) dataModel
//        .getDataInstance();
//    Map<String, Object> eventParameters = fillEventParameters(responseModel.getEntity()
//        .getId());
//    eventParameters.put(ProcessConstants.ACTION, "modified");
//    ICamundaBroadcastEventModel camundaBroadcastEventModel = new CamundaBroadcastEventModel(
//        "AfterSaveTaxonomy", eventParameters);
//    broadcastEventStrategy.execute(camundaBroadcastEventModel);
//  }
//
//  private void broadcastCreateAttributionTaxonomy(IDiExportDataModel dataModel) throws Exception
//  {
//    IGetMasterTaxonomyWithoutKPModel responseModel = (IGetMasterTaxonomyWithoutKPModel) dataModel
//        .getDataInstance();
//    Map<String, Object> eventParameters = fillEventParameters(responseModel.getEntity()
//        .getId());
//    eventParameters.put(ProcessConstants.ACTION, "created");
//    ICamundaBroadcastEventModel camundaBroadcastEventModel = new CamundaBroadcastEventModel(
//        "AfterCreateAttributionTaxonomy", eventParameters);
//    broadcastEventStrategy.execute(camundaBroadcastEventModel);
//  }
//
//  private void broadcastSaveAttributionTaxonomy(IDiExportDataModel dataModel) throws Exception
//  {
//    IGetHierarchyTaxonomyWithoutKPModel responseModel = (IGetHierarchyTaxonomyWithoutKPModel) dataModel
//        .getDataInstance();
//    Map<String, Object> eventParameters = fillEventParameters(responseModel.getEntity()
//        .getId());
//    eventParameters.put(ProcessConstants.ACTION, "modified");
//    ICamundaBroadcastEventModel camundaBroadcastEventModel = new CamundaBroadcastEventModel(
//        "AfterSaveAttributionTaxonomy", eventParameters);
//    broadcastEventStrategy.execute(camundaBroadcastEventModel);
//  }
//
//  private void broadcastBulkCreateAttributionTaxonomy(IDiExportDataModel dataModel) throws Exception
//  {
//    IBulkCreateTaxonomyResponseModel responseModel = (IBulkCreateTaxonomyResponseModel) dataModel
//        .getDataInstance();
//    List<IGetAttributionTaxonomyModel> successList = (List<IGetAttributionTaxonomyModel>) responseModel
//        .getSuccess();
//    for (IGetAttributionTaxonomyModel taxonomy : successList) {
//      Map<String, Object> eventParameters = fillEventParameters(taxonomy.getEntity()
//          .getId());
//      eventParameters.put(ProcessConstants.ACTION, "created");
//      ICamundaBroadcastEventModel camundaBroadcastEventModel = new CamundaBroadcastEventModel(
//          "AfterCreateTaxonomy", eventParameters);
//      broadcastEventStrategy.execute(camundaBroadcastEventModel);
//    }
//  }
//
//  private void broadcastBulkCreateTaxonomy(IDiExportDataModel dataModel) throws Exception
//  {
//    IBulkCreateLinkedTaxonomyResponseModel responseModel = (IBulkCreateLinkedTaxonomyResponseModel) dataModel
//        .getDataInstance();
//    List<IGetHierarchyTaxonomyModel> successList = (List<IGetHierarchyTaxonomyModel>) responseModel
//        .getSuccess();
//    for (IGetHierarchyTaxonomyModel taxonomy : successList) {
//      Map<String, Object> eventParameters = fillEventParameters(taxonomy.getId());
//      eventParameters.put(ProcessConstants.ACTION, "created");
//      ICamundaBroadcastEventModel camundaBroadcastEventModel = new CamundaBroadcastEventModel(
//          "AfterCreateTaxonomy", eventParameters);
//      broadcastEventStrategy.execute(camundaBroadcastEventModel);
//    }
//  }
//
//  private void broadcastDeleteTaxonomyLevel(IDiExportDataModel dataModel) throws Exception
//  {
//    IGetHierarchyTaxonomyWithoutKPModel responseModel = (IGetHierarchyTaxonomyWithoutKPModel) dataModel
//        .getDataInstance();
//    List<ITreeEntity> children = (List<ITreeEntity>) responseModel.getEntity()
//        .getChildren();
//    for (ITreeEntity childTaxonomy : children) {
//      String taxonomyId = childTaxonomy.getId();
//      broadcastEvent(taxonomyId);
//    }
//    if (children.size() == 0) {
//      broadcastEvent(responseModel.getEntity()
//          .getId());
//    }
//  }
//
//  private void broadcastDeleteAttributionTaxonomyLevel(IDiExportDataModel dataModel)
//      throws Exception
//  {
//    IGetMasterTaxonomyWithoutKPModel responseModel = (IGetMasterTaxonomyWithoutKPModel) dataModel
//        .getDataInstance();
//    List<ITreeEntity> children = (List<ITreeEntity>) responseModel.getEntity()
//        .getChildren();
//    for (ITreeEntity childTaxonomy : children) {
//      String taxonomyId = childTaxonomy.getId();
//      broadcastEvent(taxonomyId);
//    }
//    if (children.size() == 0) {
//      broadcastEvent(responseModel.getEntity()
//          .getId());
//    }
//  }
//
//  private void broadcastEvent(String taxonomyId) throws Exception
//  {
//    Map<String, Object> eventParameters = fillEventParameters(taxonomyId);
//    eventParameters.put(ProcessConstants.ACTION, "modified");
//    ICamundaBroadcastEventModel camundaBroadcastEventModel = new CamundaBroadcastEventModel(
//        "DeleteTaxonomyLevel", eventParameters);
//    // broadcastEventStrategy.execute(camundaBroadcastEventModel);
//  }
//
//  private Boolean manualTransfer(String instanceId, String endpointId, String organizationId,
//      String baseType)
//  {
//    Boolean isTransferSuccess;
//    try {
//      isTransferSuccess = transferToDestinationCatalog(instanceId, endpointId, organizationId,
//          baseType);
//    }
//    catch (Exception e) {
//      isTransferSuccess = false;
//      RDBMSLogger.instance().exception(e);
//    }
//    return isTransferSuccess;
//  }
//
//  private Map<String, Object> prepareEventParameterMap(IModel klassInstanceModel, String articleId,
//      String physicalCatalogId, IGetProcessExportEndpointModel getProcessExportEndpointModel,
//      String endpointId, IGetProcessEndpointEventModel getProcessEndpointEventModel)
//  {
//    Map<String, Object> eventParameters = new HashMap<String, Object>();
//    addBaseEventParameters(eventParameters);
//    eventParameters.put(ProcessConstants.KLASS_INSTANCE_ID, articleId);
//    eventParameters.put(ProcessConstants.ORGANIZATION_ID,
//        getProcessExportEndpointModel.getOrganization());
//    eventParameters.put(ProcessConstants.ENDPOINT_ID, endpointId);
//    eventParameters.put(ProcessConstants.PHYSICAL_CATALOG_ID, physicalCatalogId);
//    eventParameters.put(ProcessConstants.DATA, prepareDataMap(klassInstanceModel));
//    eventParameters.put(ProcessConstants.PROCESS_TYPE, CommonConstants.JMS_OFFBOARDING_PROCESS);
//    eventParameters.put(IComponentModel.TRANSACTION_THREAD_DATA,
//        controllerThread.getTransactionData());
//    eventParameters.put(ProcessConstants.PROCESS_ENTITY_ID, getProcessEndpointEventModel.getId());
//
//    return eventParameters;
//  }
//
//  private void addBaseEventParameters(Map<String, Object> eventParameters)
//  {
//    eventParameters.put(IComponentModel.ID_TRANSACTION, UUID.randomUUID()
//        .toString());
//    eventParameters.put(ProcessConstants.MINUTES_BEFORE_RETRY,
//        minutesBeforeRetries != null ? minutesBeforeRetries : 5);
//    eventParameters.put(ProcessConstants.NUMBER_OF_RETRIES,
//        numberOfRetries != null ? numberOfRetries : 3);
//  }
//
//  private Boolean transferToDestinationCatalog(String id, String endpointId, String organizationId,
//      String baseType) throws Exception
//  {
//    /*
//      ITransferDataRequestModel transferModel = new TransferDataRequestModel();
//      List<String> ids = new ArrayList<>();
//      ids.add(id);
//      setIdsAccordingToBaseType(ids, transferModel, baseType);
//
//      transferModel.setDestinationEndpointId(endpointId);
//      transferModel.setDestinationOrganizationId(organizationId);
//      transferModel.setDestinationCatalogId(Constants.DATA_INTEGRATION_CATALOG_IDS);
//      IIdParameterModel transferResponseModel = (IIdParameterModel) manualTransferNew.execute(transferModel);
//
//      String processInstanceId = transferResponseModel.getId();
//      getProcesstStatusOnCompletion(processInstanceId);
//
//      IGetAllSourceDestinationIdsModel getAllSourceDestinationIdsModel = new GetAllSourceDestinationIdsModels();
//      getAllSourceDestinationIdsModel.setProcessInstanceId(processInstanceId);
//      setComponentIdAccordingToBaseType(baseType, getAllSourceDestinationIdsModel);
//
//
//      ISourceDestinationResponseModel sourceDestinationIdModel = getSourceDestinationIdsByEntityTypeStrategy.execute(getAllSourceDestinationIdsModel);
//
//      Map<String, String> sourceDestinationMap = sourceDestinationIdModel.getSourceDestination();
//      Set<String> transferredIds = sourceDestinationMap.keySet();
//
//      if (ids.size() == transferredIds.size()) {
//        return true;
//      }
//      else {
//        return false;
//      }
//    */
//    return null;
//  }
//
//  private void setComponentIdAccordingToBaseType(String baseType,
//      IGetAllSourceDestinationIdsModel getAllSourceDestinationIdsModel)
//  {
//    switch (baseType) {
//      case Constants.ARTICLE_INSTANCE_BASE_TYPE:
//        getAllSourceDestinationIdsModel
//            .setComponentId(ProcessConstants.TRANSFER_ARTICLES_TO_DESTINATION_COMPONENT);
//        break;
//      case Constants.ASSET_INSTANCE_BASE_TYPE:
//        getAllSourceDestinationIdsModel
//            .setComponentId(ProcessConstants.TRANSFER_ASSETS_TO_DESTINATION_COMPONENT);
//        break;
//    }
//  }
//
//  private void setIdsAccordingToBaseType(List<String> ids, ITransferDataRequestModel transferModel,
//      String baseType)
//  {
//    switch (baseType) {
//      case Constants.ARTICLE_INSTANCE_BASE_TYPE:
//        transferModel.setArticleIdsToTransfer(ids);
//        break;
//      case Constants.ASSET_INSTANCE_BASE_TYPE:
//        transferModel.setAssetIdsToTransfer(ids);
//        break;
//    }
//  }
//
//  public void getProcesstStatusOnCompletion(String processInstanceId) throws Exception
//  {
//    ProcessStatusDetailsModel model = new ProcessStatusDetailsModel();
//    model.setProcessInstanceId(processInstanceId);
//
//    while (true) {
//      Boolean currentStatus = true;
//      List<IProcessStatusDetailsModel> processStatusDetailsModels = (List<IProcessStatusDetailsModel>) getProcessStatusByProcessInstanceIdStrategy
//          .execute(model)
//          .getList();
//      for (int i = 0; i < processStatusDetailsModels.size(); i++) {
//        if (processStatusDetailsModels.get(i)
//            .getEndTime() == null) {
//          currentStatus = false;
//        }
//      }
//
//      if (currentStatus == true)
//        break;
//      Thread.sleep(5000);
//    }
//  }
//
//  private Map<String, Object> fillEventParameters(String taxonomyId)
//  {
//    Map<String, Object> eventParameters = new HashMap<String, Object>();
//    addBaseEventParameters(eventParameters);
//    List<String> taxonomyIds = new ArrayList<>();
//    taxonomyIds.add(taxonomyId);
//    eventParameters.put(ProcessConstants.TAXONOMY_IDS, taxonomyIds);
//    eventParameters.put(ProcessConstants.PROCESS_TYPE, CommonConstants.JMS_OFFBOARDING_PROCESS);
//    return eventParameters;
//  }
//
//  protected Boolean checkViolation(IDiExportDataModel dataModel) throws Exception
//  {
//    /*
//      IGetKlassInstanceCustomTabModel getResoponseModel = null;
//      IKlassInstanceSaveModel klassInstanceSaveModel = (IKlassInstanceSaveModel) dataModel.getDataInstance();
//      switch(klassInstanceSaveModel.getBaseType()) {
//        case Constants.ARTICLE_INSTANCE_BASE_TYPE:
//          getResoponseModel = getArticleInstanceForOverviewTab.execute(getTreeModel(dataModel.getKlassInstanceId()));
//          break;
//        case Constants.ASSET_INSTANCE_BASE_TYPE:
//          getResoponseModel = getAssetInstanceForOverviewTab.execute(getTreeModel(dataModel.getKlassInstanceId()));
//          break;
//      }
//      IContentInstance klassInstance = getResoponseModel.getKlassInstance();
//
//      Boolean isRedObject = klassInstance.getMessages().getIsRed();
//      Boolean isRed = isRedObject == null ? false : (Boolean)isRedObject;
//      if(isRed){
//        return true;
//      }
//      List<IRuleViolation> ruleViolations = klassInstance.getRuleViolation();
//      for(IRuleViolation ruleViolation: ruleViolations) {
//        if(ruleViolation.getColor().equals("red")) {
//          return true;
//        }
//      }
//      List<IContentAttributeInstance> attributes = (List<IContentAttributeInstance>) klassInstance.getAttributes();
//      for(IContentAttributeInstance contentAttribute: attributes) {
//        if(contentAttribute instanceof IAttributeInstance) {
//          IAttributeInstance attribute = (IAttributeInstance)contentAttribute;
//          if((attribute.getIsMandatoryViolated()!= null && attribute.getIsMandatoryViolated())|| contentAttribute.getIsUnique() == 0) {
//            return true;
//          }
//        }
//      }
//      return false;
//    */
//    return null;
//  }
//
//  protected IGetInstanceRequestModel getTreeModel(String klassInstanceId)
//  {
//    IGetInstanceRequestModel getModel = new GetInstanceRequestModel();
//    getModel.setId(klassInstanceId);
//    return getModel;
//  }
//}
