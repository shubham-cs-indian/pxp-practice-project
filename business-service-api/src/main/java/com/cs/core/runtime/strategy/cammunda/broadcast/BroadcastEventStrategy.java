package com.cs.core.runtime.strategy.cammunda.broadcast;
//
//import com.cs.constants.CommonConstants;
//import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
//import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
//import com.cs.core.config.interactor.model.transfer.ITransferDataResponseModel;
//import com.cs.core.config.strategy.usecase.processevent.IGetProcessByConfigStrategy;
//import com.cs.core.runtime.interactor.constants.application.ProcessConstants;
//import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
//import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
//import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
//import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
//import com.cs.core.runtime.interactor.exception.camunda.WorkflowEngineException;
//import com.cs.core.runtime.interactor.model.attribute.IModifiedContentAttributeInstanceModel;
//import com.cs.core.runtime.interactor.model.camunda.ICamundaBroadcastEventModel;
//import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
//import com.cs.core.runtime.interactor.model.configuration.IModel;
//import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
//import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
//import com.cs.core.runtime.interactor.model.klassinstance.IGetInstanceTypesResponseModel;
//import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
//import com.cs.core.runtime.interactor.model.logger.ITransactionData;
//import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
//import com.cs.di.workflow.trigger.event.BusinessProcessTriggerModel;
//import com.cs.di.workflow.trigger.event.IBusinessProcessTriggerModel;
//import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
//
//import org.camunda.bpm.engine.ProcessEngineException;
//import org.camunda.bpm.engine.RuntimeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@Component("broadcastEventStrategy")
//public class BroadcastEventStrategy extends AbstractCamundaStrategy
//    implements IBroadcastEventStrategy {
//
//  @Autowired
//  protected TransactionThreadData       transactionThread;
//  @Autowired
//  protected IGetProcessByConfigStrategy getProcessByConfigStrategy;
//  @Autowired
//  RuntimeService                        runtimeService;
//  @Autowired
//  ISessionContext                       context;
//
//  public IModel execute(ICamundaBroadcastEventModel camundaBroadcastEventModel) throws Exception
//  {
//    try {
//      Map<String, Object> parameters = camundaBroadcastEventModel.getEventParameters();
//      IBusinessProcessTriggerModel requestModel = new BusinessProcessTriggerModel();
//      boolean otherEvent = false;
//      ITransactionData transactionData = transactionThread.getTransactionData();
//
//      switch (camundaBroadcastEventModel.getEventName()) {
//        case ProcessConstants.AFTER_SAVE:
//          IKlassInstanceSaveModel klassInstanceSaveModel = (IKlassInstanceSaveModel) parameters
//              .get("requestMap");
//          requestModel.setTaxonomyIds(klassInstanceSaveModel.getSelectedTaxonomyIds());
//          requestModel.setTriggeringType(camundaBroadcastEventModel.getEventName());
//          requestModel.setKlassIds(klassInstanceSaveModel.getTypes());
//          List<String> attributeIds = new ArrayList<>();
//          for (IModifiedContentAttributeInstanceModel modifiedAttribute : klassInstanceSaveModel
//              .getModifiedAttributes()) {
//            attributeIds.add(modifiedAttribute.getAttributeId());
//          }
//          for (IContentAttributeInstance addedAttribute : klassInstanceSaveModel
//              .getAddedAttributes()) {
//            attributeIds.add(addedAttribute.getAttributeId());
//          }
//          requestModel.setAttributeIds(attributeIds);
//          List<String> tagIds = new ArrayList<>();
//          for (IModifiedContentTagInstanceModel modifiedTag : klassInstanceSaveModel
//              .getModifiedTags()) {
//            tagIds.add(modifiedTag.getTagId());
//          }
//          for (ITagInstance addedTag : klassInstanceSaveModel.getAddedTags()) {
//            tagIds.add(addedTag.getTagId());
//          }
//          requestModel.setTagIds(tagIds);
//          break;
//        case ProcessConstants.AFTER_CREATE:
//          ICreateInstanceModel klassInstanceCreateModel = (ICreateInstanceModel) parameters
//              .get("requestMap");
//          requestModel.setTriggeringType(camundaBroadcastEventModel.getEventName());
//          List<String> klassIds = new ArrayList<String>();
//          klassIds.add(klassInstanceCreateModel.getType());
//          requestModel.setKlassIds(klassIds);
//          break;
//        case ProcessConstants.AFTER_SAVE_RELATIONSHIP:
//        case ProcessConstants.AFTER_SAVE_EMBEDDED:
//          Object model = parameters.get(ProcessConstants.REQUEST_MAP);
//          IContentInstance articleInstance = null;
//          if(model instanceof IGetKlassInstanceModel){
//            articleInstance = ((IGetKlassInstanceModel) model).getKlassInstance();
//          }else if(model instanceof IContentInstance){
//            articleInstance = (IContentInstance) model;
//          }
//          requestModel.setTaxonomyIds(articleInstance.getSelectedTaxonomyIds());
//          requestModel.setTriggeringType(ProcessConstants.AFTER_SAVE);
//          requestModel.setKlassIds(articleInstance.getTypes());
//          requestModel.setAttributeIds(new ArrayList<>());
//          requestModel.setTagIds(new ArrayList<>());
//          break;
//
//        case ProcessConstants.TRANSFER:
//          ITransferDataResponseModel transferDataResponseModel = (ITransferDataResponseModel) parameters
//              .get("requestMap");
//          IGetInstanceTypesResponseModel typesModel = transferDataResponseModel.getTypesModel();
//          IContentTypeIdsInfoModel typeIdsInfoModel = typesModel.getInstancesTypes()
//              .get(camundaBroadcastEventModel.getKlassInstanceId());
//          requestModel.setTaxonomyIds(typeIdsInfoModel.getTaxonomyIds());
//          requestModel.setTriggeringType(camundaBroadcastEventModel.getEventName());
//          requestModel.setKlassIds(typeIdsInfoModel.getKlassIds());
//          break;
//
//        case ProcessConstants.AFTER_IMPORT:
//          IKlassInstance klassInstance = (IKlassInstance) parameters.get("requestMap");
//          requestModel.setTaxonomyIds(klassInstance.getSelectedTaxonomyIds());
//          requestModel.setTriggeringType(camundaBroadcastEventModel.getEventName());
//          requestModel.setKlassIds(klassInstance.getTypes());
//          requestModel.setEndpointId(transactionData.getEndpointId());
//          break;
//
//        default:
//          otherEvent = true;
//      }
//      if (camundaBroadcastEventModel.getKlassInstanceId() != null) {
//        parameters.put("klassInstanceId", camundaBroadcastEventModel.getKlassInstanceId());
//      }
//      parameters.put(ISessionContext.USER_ID, context.getUserId());
//      parameters.put(ISessionContext.USER_SESSION_DTO, context.getUserSessionDTO());
//      parameters.put(CommonConstants.TRANSACTION_DATA, transactionData);
//      requestModel.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
//      requestModel.setEndpointId(transactionData.getEndpointId());
//      //setTransactionAndUserData(parameters, transactionData);
//      IIdsListParameterModel processDefinitionIdsList = getProcessByConfigStrategy
//          .execute(requestModel);
//      for (String processId : processDefinitionIdsList.getIds()) {
//        runtimeService.startProcessInstanceById(processId, parameters);
//      }
//      if (otherEvent) {
//        runtimeService.signalEventReceived(camundaBroadcastEventModel.getEventName(), parameters);
//      }
//      return null;
//    }
//    catch (ProcessEngineException e) {
//      throw new WorkflowEngineException(e);
//    }
//  }
//
////  private void setTransactionAndUserData(Map<String, Object> parameters,
////      ITransactionData transactionData)
////  {
////    parameters.put(ProcessConstants.PHYSICAL_CATALOG_ID, transactionData.getPhysicalCatalogId());
////    parameters.put(ProcessConstants.ORGANIZATION_ID, transactionData.getOrganizationId());
////    parameters.put(ProcessConstants.ENDPOINT_ID, transactionData.getEndpointId());
////    parameters.put(ProcessConstants.LOGICAL_CATALOG_ID, transactionData.getLogicalCatalogId());
////    parameters.put(CommonConstants.SYSTEM_ID, transactionData.getSystemId());
////    parameters.put(CommonConstants.DATA_LANGUAGE, transactionData.getDataLanguage());
////    parameters.put(CommonConstants.UI_LANGUAGE, transactionData.getUiLanguage());
////    parameters.put(ISessionContext.USER_ID, context.getUserId());
////  }
//}
