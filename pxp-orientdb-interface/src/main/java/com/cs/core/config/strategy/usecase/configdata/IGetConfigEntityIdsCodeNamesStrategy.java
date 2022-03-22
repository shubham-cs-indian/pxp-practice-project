package com.cs.core.config.strategy.usecase.configdata;

import com.cs.core.config.interactor.model.configdetails.IGetConfigEntityIdsCodeNamesRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigEntityIdsCodeNamesResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetConfigEntityIdsCodeNamesStrategy extends
    IConfigStrategy<IGetConfigEntityIdsCodeNamesRequestModel, IGetConfigEntityIdsCodeNamesResponseModel> {
  
}
