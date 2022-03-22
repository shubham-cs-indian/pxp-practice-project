package com.cs.di.config.interactor.modeler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.workflow.base.IAbstractTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.di.config.model.modeler.GetAllTaskMetadataResponseModel;
import com.cs.di.config.model.modeler.IGetAllTaskMetadataResponseModel;
import com.cs.di.config.model.modeler.IWorkflowTaskRequestModel;
import com.cs.workflow.base.TaskType;
import com.cs.workflow.base.TasksFactory;

@Service
public class GetAllTasksMetadata
    extends AbstractGetConfigInteractor<IWorkflowTaskRequestModel, IGetAllTaskMetadataResponseModel>
    implements IGetAllTasksMetadata {

	@Autowired
	TasksFactory tasksFactory;

	@Override
	public IGetAllTaskMetadataResponseModel executeInternal(IWorkflowTaskRequestModel requestModel) throws Exception {
		Map<TaskType, List<String>> tasksMap = new HashMap<TaskType, List<String>>();
		for (String taskId : tasksFactory.getTaskIds()) {
			IAbstractTask task = tasksFactory.getTask(taskId);
			if (!tasksMap.containsKey(task.getTaskType())) {
				tasksMap.put(task.getTaskType(), new ArrayList<String>());
			}
			tasksMap.get(task.getTaskType()).add(taskId);
		}
		IGetAllTaskMetadataResponseModel model = new GetAllTaskMetadataResponseModel();
		model.setTasksMap(tasksMap);
		return model;
	}
}
