package com.cs.core.config.strategy.usecase.rulelist;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.rulelist.IGetAllRuleListsResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllRuleListStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetAllRuleListsResponseModel> {
  
}
