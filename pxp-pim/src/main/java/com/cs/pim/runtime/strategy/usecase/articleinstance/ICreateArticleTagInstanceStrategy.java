package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;
import com.cs.pim.runtime.interactor.model.articleinstance.ICreateTagInstanceStrategyModel;

public interface ICreateArticleTagInstanceStrategy
    extends IRuntimeStrategy<ICreateTagInstanceStrategyModel, VoidModel> {
  
}
