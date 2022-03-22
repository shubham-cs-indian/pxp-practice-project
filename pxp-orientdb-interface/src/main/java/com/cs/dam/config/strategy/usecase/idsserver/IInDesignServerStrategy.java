package com.cs.dam.config.strategy.usecase.idsserver;

import com.cs.core.config.strategy.configuration.base.IStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IInDesignServerStrategy<P extends IModel, R extends IModel>
    extends IStrategy<P, R> {
  
}
