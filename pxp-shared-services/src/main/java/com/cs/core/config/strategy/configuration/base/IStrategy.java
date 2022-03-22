package com.cs.core.config.strategy.configuration.base;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IStrategy<P extends IModel, R extends IModel> {
  
  public R execute(P model) throws Exception;
}
