package com.cs.core.runtime.strategy.usecase.loadbalancer;

import com.cs.core.config.strategy.configuration.base.IStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICSLoadBalancer extends IStrategy<IModel, IModel> {
  
}
