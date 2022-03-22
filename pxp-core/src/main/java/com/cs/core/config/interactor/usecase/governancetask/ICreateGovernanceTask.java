package com.cs.core.config.interactor.usecase.governancetask;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.task.ICreateGovernanceTaskResponseModel;
import com.cs.core.config.interactor.model.task.ITaskModel;

public interface ICreateGovernanceTask extends ICreateConfigInteractor<ITaskModel, ICreateGovernanceTaskResponseModel> {
  
}
