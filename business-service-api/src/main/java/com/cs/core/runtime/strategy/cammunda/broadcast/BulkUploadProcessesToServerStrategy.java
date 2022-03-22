package com.cs.core.runtime.strategy.cammunda.broadcast;

import java.util.HashMap;
import java.util.Map;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.xml.instance.DomDocument;
import org.camunda.bpm.model.xml.instance.DomElement;
import org.springframework.stereotype.Service;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.processevent.BulkSaveProcessEventResponseModel;
import com.cs.core.config.interactor.model.processevent.IBulkSaveProcessEventResponseModel;
import com.cs.core.config.interactor.model.processevent.IGetValidationInformationModel;
import com.cs.core.config.interactor.model.processevent.IProcessEventModel;
import com.cs.core.config.interactor.model.processevent.IUploadProcessEventsResponseModel;
import com.cs.core.config.interactor.model.processevent.UploadProcessEventsResponseModel;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.exception.process.InvalidWorkflowException;

@Service
public class BulkUploadProcessesToServerStrategy extends AbstractCamundaStrategy
    implements IBulkUploadProcessToServerStrategy {
  
  public static final String  START_EVENT             = "startEvent";
  public static final String  TIMER_EVENT_DEFINITION  = "timerEventDefinition";
  public static final String  BPMN_TFORMAL_EXPRESSION = "bpmn:tFormalExpression";
  private static final String XSI                     = "http://www.w3.org/2001/XMLSchema-instance";
  
  @Override
  public IBulkSaveProcessEventResponseModel execute(IListModel<? extends IGetValidationInformationModel> requestModel) throws Exception
  {
    IBulkSaveProcessEventResponseModel returnModel = new BulkSaveProcessEventResponseModel();
    IUploadProcessEventsResponseModel successEventList = new UploadProcessEventsResponseModel();
    Map<String, Exception> failure = new HashMap<String, Exception>();
    for (IGetValidationInformationModel process : requestModel.getList()) {
      try {
        BpmnModelInstance bpmnModel = convertBpmnXmlToBpmnModel(process.getProcessDefinition());
        String processName = process.getLabel();
        DomDocument document = bpmnModel.getDocument();
        DomElement root = document.getRootElement();
        DomElement processElement = parseProcessElement(root, PROCESS);
        processElement.setAttribute(IProcessEventModel.IS_EXECUTABLE,
            Boolean.toString(process.getIsExecutable()));
        if (isValid(process.getTimerDefinitionType())
            && isValid(process.getTimerStartExpression())) {
          setTimerStartInformation(document, processElement, process.getTimerDefinitionType(),
              process.getTimerStartExpression());
        }
        ProcessDefinition processDefinition = deployProcessInCamunda(bpmnModel, processName);
        deleteOldProcessDefinition(process, processDefinition);
        process.setProcessDefinition(Bpmn.convertToString(bpmnModel));
        if (process.getValidationInfo() == null || process.getValidationInfo().getIsWorkflowValid()) {
          successEventList.getProcessEventsList().add(process);
        }
        else {
          failure.put(process.getId(), new InvalidWorkflowException("InvalidWorkFlow"));
        }
      }
      catch (Exception e) {
        RDBMSLogger.instance()
            .exception(e);
        failure.put(process.getId(), e);
      }
    }
    returnModel.setSuccess(successEventList);
    returnModel.setFailure(failure);
    return returnModel;
  }
  
  private boolean isValid(String inputString)
  {
    return inputString != null && !inputString.isBlank();
  }
  
  private void setTimerStartInformation(DomDocument document, DomElement processElement,
      String timerDefinitionType, String timerStartExpression) throws Exception
  {
    DomElement parseProcessElement = parseProcessElement(processElement, START_EVENT);
    DomElement timerDefinitionElement = parseProcessElement(parseProcessElement,
        TIMER_EVENT_DEFINITION);
    DomElement childElement = document.createElement(MODEL_NS, "bpmn:" + timerDefinitionType);
    childElement.setAttribute(XSI, "type", BPMN_TFORMAL_EXPRESSION);
    childElement.setTextContent(timerStartExpression);
    if (timerDefinitionElement != null) {
      if (!timerDefinitionElement.getChildElements()
          .isEmpty()) {
        timerDefinitionElement.replaceChild(childElement, timerDefinitionElement.getChildElements()
            .get(0));
      }
      else {
        timerDefinitionElement.appendChild(childElement);
      }
    }
  }
}
