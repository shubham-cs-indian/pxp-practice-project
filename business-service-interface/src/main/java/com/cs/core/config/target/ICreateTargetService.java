package com.cs.core.config.target;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.target.ITargetModel;

public interface ICreateTargetService
    extends ICreateConfigService<ITargetModel, IGetKlassEntityWithoutKPModel> {
  
}
