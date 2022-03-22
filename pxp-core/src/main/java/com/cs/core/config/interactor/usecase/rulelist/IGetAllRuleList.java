package com.cs.core.config.interactor.usecase.rulelist;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.rulelist.IGetAllRuleListsResponseModel;

public interface IGetAllRuleList
    extends IGetConfigInteractor<IConfigGetAllRequestModel, IGetAllRuleListsResponseModel> {
  
}
