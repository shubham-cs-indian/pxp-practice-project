package com.cs.di.config.interactor.modeler;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.di.config.model.modeler.IGetAllTaskMetadataResponseModel;
import com.cs.di.config.model.modeler.IWorkflowTaskRequestModel;

public interface IGetAllTasksMetadata extends IGetConfigInteractor<IWorkflowTaskRequestModel, IGetAllTaskMetadataResponseModel> {
  
}
