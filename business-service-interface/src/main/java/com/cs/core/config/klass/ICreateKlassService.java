package com.cs.core.config.klass;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;

public interface ICreateKlassService
    extends ICreateConfigService<IKlassModel, IGetKlassEntityWithoutKPModel> {
  
}
