package com.cs.di.workflow.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.di.runtime.entity.dao.IWorkflowStatusDAO;
import com.cs.di.runtime.entity.idto.IWorkflowStatusDTO;
import com.cs.di.runtime.entity.idto.IWorkflowStatusDTO.WFTaskStatus;
import com.cs.di.runtime.utils.DiUtils;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.cs.di.workflow.tasks.AbstractTask;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.IAbstractExecutionReader;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.WorkflowConstants;
import com.cs.workflow.base.WorkflowType;

@Component("processEndEvent")
public class ProcessEndEvent extends AbstractEvent {
	public final List<String> INPUT_LIST = new ArrayList<String>();
	public final List<String> OUTPUT_LIST = Arrays.asList(AbstractTask.EXECUTION_STATUS);
	public final List<WorkflowType> WORKFLOW_TYPES = Arrays.asList(WorkflowType.STANDARD_WORKFLOW,
			WorkflowType.SCHEDULED_WORKFLOW);
	public final List<EventType> EVENT_TYPES = Arrays.asList(EventType.BUSINESS_PROCESS,
			EventType.INTEGRATION);

	@Override
	public List<String> getInputList() {
		return INPUT_LIST;
	}

	@Override
	public List<String> getOutputList() {
		return OUTPUT_LIST;
	}

	@Override
	public List<WorkflowType> getWorkflowTypes() {
		return WORKFLOW_TYPES;
	}

	@Override
	public List<EventType> getEventTypes() {
		return EVENT_TYPES;
	}

	@Override
	public TaskType getTaskType() {
		return TaskType.DRAFT_TASK;
	}
	
  /**
   * Validate input parameters
   * @param inputFields
   */
  @Override
  public List<String> validate(Map<String, Object> inputFields)
  {
    return null;
  }
 
  /** Empty method to avoid creating workflow instance at end event .
   * @param execution
   * @param reader
   * @return null
   */
  @Override
  protected Long createTaskInstance(Object execution, IAbstractExecutionReader reader, WorkflowTaskModel model)
  {
    return null;
  }
  
   /** Update create Workflow instance when it gets completed.
   * @param model
   */
  @Override
  protected void endTaskInstance(WorkflowTaskModel model, Object execution, IAbstractExecutionReader reader, Long id)
  {
    try {
      ILocaleCatalogDAO localeCatalogDAO = DiUtils.createLocaleCatalogDAO(model.getWorkflowModel().getUserSessionDto(), model.getWorkflowModel().getTransactionData());
    //Added for WF created with No Physical catalogue in it 
      if (localeCatalogDAO == null) {
        localeCatalogDAO = DiUtils.createLocaleCatalogDAO(DiUtils.createUserSessionDto(), DiUtils.createTransactionData());
      }
      IWorkflowStatusDAO workflowStatusDAO = localeCatalogDAO.openWorkflowStatusDAO();
      IWorkflowStatusDTO statusDTO = workflowStatusDAO.newWorkflowStatusDTO();
      Long processInstanceId = Long.valueOf(reader.getProcessInstanceId(execution));
      Map<Long, Long> instanceIidMap = (Map<Long, Long>)reader.getVariable(execution, WorkflowConstants.INSTANCE_IID);
      statusDTO.setInstanceIID(instanceIidMap.get(processInstanceId));
      statusDTO.setEndTime(System.currentTimeMillis());
      statusDTO.setStatus(WFTaskStatus.valueOf(WFTaskStatus.ENDED_SUCCESS.toString()).ordinal());//TODO : it will be done with PXPFDEV-15274
      workflowStatusDAO.updateWFOrTaskStatus(statusDTO);
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
  }
}
