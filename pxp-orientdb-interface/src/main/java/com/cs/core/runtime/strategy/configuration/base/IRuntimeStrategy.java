package com.cs.core.runtime.strategy.configuration.base;

import com.cs.core.config.strategy.configuration.base.IStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IRuntimeStrategy<P extends IModel, R extends IModel> extends IStrategy<P, R> {
  
}
