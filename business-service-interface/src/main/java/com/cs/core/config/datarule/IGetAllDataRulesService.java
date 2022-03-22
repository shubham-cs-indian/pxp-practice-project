package com.cs.core.config.datarule;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.datarule.IGetAllDataRulesResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

public interface IGetAllDataRulesService extends IGetConfigService<IConfigGetAllRequestModel, IGetAllDataRulesResponseModel> {
  
}
