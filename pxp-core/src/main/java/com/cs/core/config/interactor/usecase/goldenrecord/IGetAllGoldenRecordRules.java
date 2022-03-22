package com.cs.core.config.interactor.usecase.goldenrecord;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.goldenrecord.IGetAllGoldenRecordRulesModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

public interface IGetAllGoldenRecordRules
    extends IGetConfigInteractor<IConfigGetAllRequestModel, IGetAllGoldenRecordRulesModel> {
  
}
