package com.cs.core.config.interactor.usecase.state;

import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.config.interactor.model.state.IStateModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetState extends IConfigInteractor<IIdParameterModel, IStateModel> {
  
}
