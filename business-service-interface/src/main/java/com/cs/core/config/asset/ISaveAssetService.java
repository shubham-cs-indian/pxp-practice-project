package com.cs.core.config.asset;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;

public interface ISaveAssetService
    extends ISaveConfigService<IKlassSaveModel, IGetKlassEntityWithoutKPModel> {
  
}
