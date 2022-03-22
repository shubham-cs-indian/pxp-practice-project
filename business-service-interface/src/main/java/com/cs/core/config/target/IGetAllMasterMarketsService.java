package com.cs.core.config.target;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.target.ITargetModel;

public interface IGetAllMasterMarketsService
    extends IGetConfigService<ITargetModel, IListModel<ITargetModel>> {
  
}
