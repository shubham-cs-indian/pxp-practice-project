package com.cs.core.config.interactor.usecase.asset;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;

public interface ISaveAsset
    extends ISaveConfigInteractor<IKlassSaveModel, IGetKlassEntityWithoutKPModel> {
  
}
