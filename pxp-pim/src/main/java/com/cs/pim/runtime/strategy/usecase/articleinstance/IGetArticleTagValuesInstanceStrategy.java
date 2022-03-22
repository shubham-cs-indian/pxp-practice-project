package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.tag.IGetTagValuesInstanceStrategyModel;
import com.cs.core.runtime.interactor.model.tag.IIdAndListInstanceModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetArticleTagValuesInstanceStrategy
    extends IRuntimeStrategy<IIdAndListInstanceModel, IGetTagValuesInstanceStrategyModel> {
  
}
