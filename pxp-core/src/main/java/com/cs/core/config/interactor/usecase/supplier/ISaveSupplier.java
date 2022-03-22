package com.cs.core.config.interactor.usecase.supplier;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;

public interface ISaveSupplier
    extends ISaveConfigInteractor<IKlassSaveModel, IGetKlassEntityWithoutKPModel> {
  
}
