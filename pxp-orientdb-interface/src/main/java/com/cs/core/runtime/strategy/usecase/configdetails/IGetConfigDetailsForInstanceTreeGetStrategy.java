package com.cs.core.runtime.strategy.usecase.configdetails;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetRequestModel;

public interface IGetConfigDetailsForInstanceTreeGetStrategy extends
    IConfigStrategy<IConfigDetailsForInstanceTreeGetRequestModel, IConfigDetailsForInstanceTreeGetModel> {
  
}
