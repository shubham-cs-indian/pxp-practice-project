package com.cs.core.config.strategy.usecase.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.IGetAllGoldenRecordRulesModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllGoldenRecordRulesStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetAllGoldenRecordRulesModel> {
  
}
