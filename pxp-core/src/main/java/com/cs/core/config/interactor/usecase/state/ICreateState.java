package com.cs.core.config.interactor.usecase.state;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.state.IGetStateResponseModel;
import com.cs.core.config.interactor.model.state.IStateModel;

public interface ICreateState extends ICreateConfigInteractor<IStateModel, IGetStateResponseModel> {
  
}
