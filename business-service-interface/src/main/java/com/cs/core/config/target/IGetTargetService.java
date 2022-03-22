package com.cs.core.config.target;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetTargetService extends IGetConfigService<IIdParameterModel, ITargetModel> {
  
}
