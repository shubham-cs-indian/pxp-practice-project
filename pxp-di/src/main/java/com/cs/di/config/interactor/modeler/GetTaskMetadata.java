package com.cs.di.config.interactor.modeler;

import com.cs.workflow.base.IAbstractTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.di.config.model.modeler.GetTaskMetadataResponseModel;
import com.cs.di.config.model.modeler.IGetTaskMetadataResponseModel;
import com.cs.workflow.base.TasksFactory;

@Service
public class GetTaskMetadata
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetTaskMetadataResponseModel>
    implements IGetTaskMetadata {
  
  @Autowired
  TasksFactory tasksFactory;
  
  public IGetTaskMetadataResponseModel executeInternal(IIdParameterModel requestModel) throws Exception
  {
    IGetTaskMetadataResponseModel getTaskMetadataResponseModel = new GetTaskMetadataResponseModel();
    IAbstractTask task = tasksFactory.getTask(requestModel.getId());
    getTaskMetadataResponseModel.setInputList(task.getInputList());
    getTaskMetadataResponseModel.setOutputList(task.getOutputList());
    getTaskMetadataResponseModel.setTaskType(task.getTaskType());
    return getTaskMetadataResponseModel;
  }
}
