package com.cs.core.config.interactor.usecase.datarule;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.datarule.IGetAllDataRulesResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

public interface IGetAllDataRules
    extends IGetConfigInteractor<IConfigGetAllRequestModel, IGetAllDataRulesResponseModel> {
  
}
