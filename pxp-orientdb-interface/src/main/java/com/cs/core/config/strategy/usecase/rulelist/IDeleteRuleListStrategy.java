package com.cs.core.config.strategy.usecase.rulelist;

import com.cs.core.config.interactor.model.rulelist.IBulkDeleteRuleListReturnModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IDeleteRuleListStrategy
    extends IConfigStrategy<IIdsListParameterModel, IBulkDeleteRuleListReturnModel> {
  
}
