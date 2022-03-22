package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.instance.ICreateInstanceStrategyModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface ICreateArticleInstanceStrategy
    extends IRuntimeStrategy<ICreateInstanceStrategyModel, IModel> {
  
}
