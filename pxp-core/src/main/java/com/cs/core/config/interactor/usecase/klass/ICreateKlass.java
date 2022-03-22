package com.cs.core.config.interactor.usecase.klass;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;

public interface ICreateKlass
    extends ICreateConfigInteractor<IKlassModel, IGetKlassEntityWithoutKPModel> {
  
}
