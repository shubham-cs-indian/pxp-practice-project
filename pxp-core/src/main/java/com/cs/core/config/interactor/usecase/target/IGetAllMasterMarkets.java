package com.cs.core.config.interactor.usecase.target;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.target.ITargetModel;

public interface IGetAllMasterMarkets
    extends IGetConfigInteractor<ITargetModel, IListModel<ITargetModel>> {
  
}
