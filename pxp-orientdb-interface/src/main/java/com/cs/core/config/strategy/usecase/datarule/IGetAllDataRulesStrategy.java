package com.cs.core.config.strategy.usecase.datarule;

import com.cs.core.config.interactor.model.datarule.IGetAllDataRulesResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllDataRulesStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetAllDataRulesResponseModel> {
  
}
