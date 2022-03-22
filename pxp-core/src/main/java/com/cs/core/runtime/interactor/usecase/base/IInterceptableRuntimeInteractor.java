package com.cs.core.runtime.interactor.usecase.base;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IInterceptableRuntimeInteractor<P extends IModel, R extends IModel>
    extends IRuntimeInteractor<P, R> {
  
  public R executeInternal(P dataModel) throws Exception;
  
  public abstract String getLabel();
}
