package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.config.interactor.model.variantcontext.IGetConfigDetailsForAutoCreateTIVRequestModel;
import com.cs.core.config.interactor.model.variantcontext.IGetConfigDetailsForAutoCreateTIVResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;


public interface IGetConfigDetailsForAutoCreateTIVStrategy extends
    IConfigStrategy<IGetConfigDetailsForAutoCreateTIVRequestModel, IGetConfigDetailsForAutoCreateTIVResponseModel> {
  
}
