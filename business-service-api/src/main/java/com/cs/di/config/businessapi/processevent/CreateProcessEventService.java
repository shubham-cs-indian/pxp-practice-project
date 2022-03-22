package com.cs.di.config.businessapi.processevent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.config.businessapi.base.Validations;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.exception.endpoint.InvalidEventTypeInWorkFlowException;
import com.cs.core.config.interactor.exception.endpoint.InvalidTriggeringTypeException;
import com.cs.core.config.interactor.exception.validationontype.InvalidEventTypeException;
import com.cs.core.config.interactor.model.processevent.ICreateOrSaveProcessEventResponseModel;
import com.cs.core.config.interactor.model.processevent.IProcessEventModel;
import com.cs.core.config.strategy.usecase.task.IGetProcessDefinitionByIdStrategy;
import com.cs.core.runtime.interactor.exception.process.InvalidWorkflowException;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.di.config.strategy.usecase.processevent.ICreateProcessEventStrategy;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.WorkflowConstants;

@Service
public class CreateProcessEventService extends AbstractCreateConfigService<IProcessEventModel, ICreateOrSaveProcessEventResponseModel>
    implements ICreateProcessEventService {
  
  @Autowired
  protected ICreateProcessEventStrategy       createProcessEventStrategy;
  
  @Autowired
  protected IGetProcessDefinitionByIdStrategy getProcessDefinitionByIdStrategy;
  
  public static final List<String>            TRIGGERING_EVENT_BUSINESS_PROCESS = Arrays.asList(WorkflowConstants.AFTER_CREATE,
      WorkflowConstants.AFTER_SAVE);
  public static final List<String>            TRIGGERING_EVENT_INTEGRATION      = Arrays.asList(WorkflowConstants.IMPORT,
      WorkflowConstants.EXPORT);
  
  @Override
  public ICreateOrSaveProcessEventResponseModel executeInternal(IProcessEventModel processEventModel) throws Exception
  {
    String id = processEventModel.getId();
    if (id == null || id.equals("")) {
      id = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.EVENT.getPrefix());
      processEventModel.setId(id);
    }
    
    Validations.validateLabel(processEventModel.getLabel());
    String workflowType = processEventModel.getWorkflowType();
    String eventType = processEventModel.getEventType();
    String triggeringType = processEventModel.getTriggeringType();
    validateProcessEvent(workflowType, eventType, triggeringType);
    
    if (eventType != null && eventType.equals(EventType.INTEGRATION.name())) {
      switch (triggeringType == null ? "" : triggeringType) {
        case WorkflowConstants.IMPORT:
          replaceProcessDefinationByStandaredWorkflow(processEventModel, "IMPORT");
          break;
        case WorkflowConstants.EXPORT:
          replaceProcessDefinationByStandaredWorkflow(processEventModel, "EXPORT");
          break;
      }
    }
    
    return createProcessEventStrategy.execute(processEventModel);
  }

  @SuppressWarnings("unchecked")
  private void replaceProcessDefinationByStandaredWorkflow(IProcessEventModel model, String processId) throws Exception
  {
    IIdsListParameterModel ids = new IdsListParameterModel();
    ids.setIds(Arrays.asList(processId));
    Map<String, Object> processDefinition = getProcessDefinitionByIdStrategy.execute(ids).getProcessDefinition();
    Map<String, Object> exportProcessMap = (Map<String, Object>) processDefinition.get(processId);
    if(exportProcessMap != null)
      model.setProcessDefinition((String) exportProcessMap.get("processDefinition"));
  }
  
  private void validateProcessEvent(String workflowType, String eventType, String triggeringType) throws Exception
  {
    if (StringUtils.isEmpty(workflowType)) {
      throw new InvalidWorkflowException(IProcessEventModel.WORKFLOW_TYPE + " Can't be empty ");
    }
    else {
      switch (workflowType) {
        case WorkflowConstants.JMS_WORKFLOW:
        case WorkflowConstants.SCHEDULED_WORKFLOW:
          if (StringUtils.isNotEmpty(eventType) ) {
            throw new InvalidEventTypeException("Event type must be empty");
          }
          if (StringUtils.isNotEmpty(triggeringType)) {
            throw new InvalidTriggeringTypeException("Triggering type must be empty");
          }
          break;
        
        case WorkflowConstants.STANDARD_WORKFLOW:
        case WorkflowConstants.TASKS_WORKFLOW:
          if (eventType.isEmpty() || triggeringType.isEmpty()) {
            throw new InvalidTriggeringTypeException("Event type or trigerring type can't be empty");
          }
          else if (EventType.EVENT_TYPE_LIST.contains(eventType)) {
            if (eventType.equals(EventType.BUSINESS_PROCESS.toString()) && !TRIGGERING_EVENT_BUSINESS_PROCESS.contains(triggeringType)) {
              throw new InvalidTriggeringTypeException(
                  "Triggering type for " + EventType.BUSINESS_PROCESS.toString() + " : " + triggeringType);
            }
            else if (eventType.equals(EventType.INTEGRATION.toString()) && !TRIGGERING_EVENT_INTEGRATION.contains(triggeringType)) {
              throw new InvalidTriggeringTypeException("Triggering type for " + EventType.INTEGRATION.toString() + " : " + triggeringType);
            }
          }
          else {
            throw new InvalidEventTypeInWorkFlowException();
          }
          break;
          
        case WorkflowConstants.HIDDEN_WORKFLOW:
        case WorkflowConstants.USER_SCHEDULED_WORKFLOW:
          break;
          
        default:
          throw new InvalidWorkflowException();
      }
    }
  }
}
