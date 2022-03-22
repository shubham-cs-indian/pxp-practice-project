package com.cs.core.config.goldenrecord;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.goldenrecord.IGetAllGoldenRecordRulesModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

public interface IGetAllGoldenRecordRulesService extends IGetConfigService<IConfigGetAllRequestModel, IGetAllGoldenRecordRulesModel> {
  
}
