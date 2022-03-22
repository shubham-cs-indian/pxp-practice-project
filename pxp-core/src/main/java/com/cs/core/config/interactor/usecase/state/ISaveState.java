package com.cs.core.config.interactor.usecase.state;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.state.IGetStateResponseModel;
import com.cs.core.config.interactor.model.state.IStateModel;

public interface ISaveState extends ISaveConfigInteractor<IStateModel, IGetStateResponseModel> {
  
}
