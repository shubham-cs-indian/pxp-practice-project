package com.cs.di.base;

import com.cs.core.config.strategy.configuration.base.IStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDiStrategy<P extends IModel, R extends IModel> extends IStrategy<P, R> {
}
