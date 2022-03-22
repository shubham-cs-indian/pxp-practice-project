package com.cs.di.config.businessapi.processevent;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.xml.instance.DomDocument;
import org.camunda.bpm.model.xml.instance.DomElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.CloneProcessEventModel;
import com.cs.core.config.interactor.model.mapping.IConfigCloneEntityInformationModel;
import com.cs.core.config.interactor.model.processevent.IProcessEventModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.strategy.cammunda.broadcast.IBulkUploadProcessToServerStrategy;
import com.cs.di.config.interactor.model.initializeworflowevent.BulkProcessEventSaveResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IBulkProcessEventSaveResponseModel;
import com.cs.di.config.interactor.model.initializeworflowevent.ICloneProcessEventModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetProcessEventsByIdsResponseModel;
import com.cs.di.config.strategy.usecase.processevent.ICloneProcessEventsStrategy;
import com.cs.di.config.strategy.usecase.processevent.IGetProcessEventsByIdsStrategy;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.cs.workflow.base.WorkflowType;

@Service
public class CloneProcessEventsService
    extends AbstractCreateConfigService<IListModel<IConfigCloneEntityInformationModel>, IBulkProcessEventSaveResponseModel>
    implements ICloneProcessEventsService {
  
  @Autowired
  protected IGetProcessEventsByIdsStrategy     getProcessEventsByIdsStrategy;
  
  @Autowired
  protected ICloneProcessEventsStrategy        cloneProcessEventsStrategy;
  
  @Autowired
  protected IBulkUploadProcessToServerStrategy bulkUploadProcessToServerStrategy;
  
  public static final String                   MODEL_NS                = "http://www.omg.org/spec/BPMN/20100524/MODEL";
  public static final String                   START_EVENT             = "startEvent";
  public static final String                   TIMER_EVENT_DEFINITION  = "timerEventDefinition";
  public static final String                   BPMN_TFORMAL_EXPRESSION = "bpmn:tFormalExpression";
  public static final String                   XSI                     = "http://www.w3.org/2001/XMLSchema-instance";
  public static final String                   PROCESS                 = "process";
  
  /**
   * This function will fetch src processes and then create new process models
   * respectively and send a call to plugin to create those entries in database
   */
  @Override
  public IBulkProcessEventSaveResponseModel executeInternal(IListModel<IConfigCloneEntityInformationModel> newProcessListInfoModels)
      throws Exception
  {
    IListModel<ICloneProcessEventModel> requestModel = new ListModel<>();
    List<ICloneProcessEventModel> requestModelList = new ArrayList<>();
    requestModel.setList(requestModelList);
    
    // Fetching original ids from request model
    List<String> originalProcessIds = newProcessListInfoModels.getList().stream().map(entityInfoMap -> entityInfoMap.getOriginalEntityId())
        .collect(Collectors.toList());
    IGetProcessEventsByIdsResponseModel srcProcessEvents = getProcessEventsByIdsStrategy
        .execute(new IdsListParameterModel(originalProcessIds));
    Map<String, IConfigCloneEntityInformationModel> newProcessIdCloneEndpointMap = new HashMap<String, IConfigCloneEntityInformationModel>();
    // Creating models for new processes. Copying data from the source
    // processes.
    for (IConfigCloneEntityInformationModel newProcessInfoModel : newProcessListInfoModels.getList()) {
      IProcessEventModel srcProcessEvent = srcProcessEvents.getProcessEvents().get(newProcessInfoModel.getOriginalEntityId());
      ICloneProcessEventModel destProcessModel = new CloneProcessEventModel();
      destProcessModel.setProcessDefinition(srcProcessEvent.getProcessDefinition());
      destProcessModel.setCode(newProcessInfoModel.getCode());
      destProcessModel.setId(newProcessInfoModel.getId());
      destProcessModel.setEventType(srcProcessEvent.getEventType());
      destProcessModel.setLabel(newProcessInfoModel.getLabel());
      destProcessModel.setOriginalEntityId(newProcessInfoModel.getOriginalEntityId());
      destProcessModel.setType(srcProcessEvent.getType());
      destProcessModel.setProcessType(srcProcessEvent.getProcessType());
      destProcessModel.setTriggeringType(srcProcessEvent.getTriggeringType());
      destProcessModel.setPhysicalCatalogIds(srcProcessEvent.getPhysicalCatalogIds());
      destProcessModel.setIsXMLModified(false);
      destProcessModel.setIsExecutable(srcProcessEvent.getIsExecutable());
      destProcessModel.setEntityType(srcProcessEvent.getEntityType());
      destProcessModel.setWorkflowType(srcProcessEvent.getWorkflowType());
      destProcessModel.setToSchedule(srcProcessEvent.getToSchedule());
      destProcessModel.setFromSchedule(srcProcessEvent.getFromSchedule());
      destProcessModel.setActionSubType(srcProcessEvent.getActionSubType());
      destProcessModel.setOrganizationsIds(srcProcessEvent.getOrganizationsIds());
      updateIsExecutableInProcessDefinition(destProcessModel);
      if (Boolean.TRUE.equals(newProcessInfoModel.getIsFromTemplate())) {
        ModifyDataForUserScheduleWorkflow(destProcessModel, newProcessInfoModel);
      }
      requestModelList.add(destProcessModel);
      newProcessIdCloneEndpointMap.put(destProcessModel.getId(), newProcessInfoModel);
    }
    
    IBulkProcessEventSaveResponseModel responseModel;
    if (!requestModelList.isEmpty()) {
      responseModel = cloneProcessEventsStrategy.execute(requestModel);
    }
    else {
      responseModel = new BulkProcessEventSaveResponseModel();
    }
    
    return responseModel;
  }
  
  /**
   * Modify isExecutable in ProcessDefinition while cloning
   * 
   * @param destProcessModel
   * @throws Exception
   */
  private void updateIsExecutableInProcessDefinition(ICloneProcessEventModel destProcessModel) throws Exception
  {
    BpmnModelInstance bpmnModel = Bpmn.readModelFromStream(new ByteArrayInputStream(destProcessModel.getProcessDefinition().getBytes()));
    DomDocument document = bpmnModel.getDocument();
    DomElement root = document.getRootElement();
    DomElement processElement = parseProcessElement(root, PROCESS);
    processElement.setAttribute(IProcessEventModel.IS_EXECUTABLE, Boolean.toString(Boolean.FALSE));
    destProcessModel.setProcessDefinition(Bpmn.convertToString(bpmnModel));
  }
  
  /**
   * Modify timer expression and activation while cloning
   * user_scheduled_workflow
   * 
   * @param destProcessModel
   * @param newProcessInfoModel
   * @throws Exception
   */
  private void ModifyDataForUserScheduleWorkflow(ICloneProcessEventModel destProcessModel,
      IConfigCloneEntityInformationModel newProcessInfoModel) throws Exception
  {
    destProcessModel.setWorkflowType(WorkflowType.USER_SCHEDULED_WORKFLOW.name());
    destProcessModel.setPhysicalCatalogIds(newProcessInfoModel.getPhysicalCatalogIds());
    if (!DiValidationUtil.isBlank(newProcessInfoModel.getTimerStartExpression())
        && !DiValidationUtil.isBlank(newProcessInfoModel.getTimerDefinitionType())) {
      BpmnModelInstance bpmnModel = Bpmn.readModelFromStream(new ByteArrayInputStream(destProcessModel.getProcessDefinition().getBytes()));
      DomDocument document = bpmnModel.getDocument();
      DomElement root = document.getRootElement();
      DomElement processElement = parseProcessElement(root, PROCESS);
      processElement.setAttribute(IProcessEventModel.IS_EXECUTABLE, Boolean.toString(Boolean.FALSE));
      DomElement parseProcessElement = parseProcessElement(processElement, START_EVENT);
      DomElement timerDefinitionElement = parseProcessElement(parseProcessElement, TIMER_EVENT_DEFINITION);
      DomElement childElement = document.createElement(MODEL_NS, "bpmn:" + newProcessInfoModel.getTimerDefinitionType());
      childElement.setAttribute(XSI, "type", BPMN_TFORMAL_EXPRESSION);
      childElement.setTextContent(newProcessInfoModel.getTimerStartExpression());
      if (timerDefinitionElement != null) {
        if (!timerDefinitionElement.getChildElements().isEmpty()) {
          timerDefinitionElement.replaceChild(childElement, timerDefinitionElement.getChildElements().get(0));
        }
        else {
          timerDefinitionElement.appendChild(childElement);
        }
      }
      destProcessModel.setProcessDefinition(Bpmn.convertToString(bpmnModel));
    }
  }
  
  /**
   * @param root - root Dom element of xml
   * @param elementName - Element label
   * @return DemElement with name as "process"
   * @throws Exception
   */
  protected DomElement parseProcessElement(DomElement root, String elementName) throws Exception
  {
    List<DomElement> elements = root.getChildElementsByNameNs(MODEL_NS, elementName);
    if (elements.isEmpty()) {
      throw new Exception("No BPMN Process found in the Workflow : " + elementName);
    }
    if (elements.size() > 1) {
      throw new Exception("Multiple BPMN Processes/Pools found in the Workflow : " + elementName);
    }
    return elements.get(0);
  }
  
  @Override
  public ServiceType getServiceType()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
}
