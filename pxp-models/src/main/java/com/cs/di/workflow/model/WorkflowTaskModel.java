package com.cs.di.workflow.model;

import java.util.HashMap;
import java.util.Map;

import com.cs.di.workflow.model.executionstatus.ExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;

public class WorkflowTaskModel {
	private Map<String, Object> inputParameters = new HashMap<>();
	private Map<String, Object> outputParameters = new HashMap<>();
	private IExecutionStatus<? extends IOutputExecutionStatusModel> executionStatusTable = new ExecutionStatus();
	private String taskId;
	private WorkflowModel workflowModel;

	public Map<String, Object> getInputParameters() {
		if (inputParameters == null) {
			inputParameters = new HashMap<String, Object>();
		}
		return inputParameters;
	}

	public void setInputParameters(Map<String, Object> inputParameters) {
		this.inputParameters = inputParameters;
	}

	public Map<String, Object> getOutputParameters() {
		if (outputParameters == null) {
			outputParameters = new HashMap<String, Object>();
		}
		return outputParameters;
	}

	public void setOutputParameters(Map<String, Object> outputParameters) {
		this.outputParameters = outputParameters;
	}

	public IExecutionStatus<? extends IOutputExecutionStatusModel> getExecutionStatusTable() {
		return executionStatusTable;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public WorkflowModel getWorkflowModel() {
		return workflowModel;
	}

	public void setWorkflowModel(WorkflowModel workflowModel) {
		this.workflowModel = workflowModel;
	}
}
