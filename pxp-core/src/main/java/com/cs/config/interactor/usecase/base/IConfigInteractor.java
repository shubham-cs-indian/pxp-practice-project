package com.cs.config.interactor.usecase.base;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.usecase.base.IInteractor;

public interface IConfigInteractor<P extends IModel, R extends IModel> extends IInteractor<P, R> {
  
}
