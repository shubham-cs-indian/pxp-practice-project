package com.cs.core.config.rulelist;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.rulelist.IGetAllRuleListsResponseModel;

public interface IGetAllRuleListService extends IGetConfigService<IConfigGetAllRequestModel, IGetAllRuleListsResponseModel> {
  
}
