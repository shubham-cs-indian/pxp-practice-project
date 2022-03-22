package com.cs.core.config.interactor.usecase.governancetask;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetGovernanceTask extends IGetConfigInteractor<IIdParameterModel, ITaskModel> {
  
}
