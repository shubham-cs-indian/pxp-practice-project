package com.cs.config.interactor.usecase.base;

import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetConfigInteractor<P extends IModel, R extends IModel>
    extends IConfigInteractor<P, R> {
  
}