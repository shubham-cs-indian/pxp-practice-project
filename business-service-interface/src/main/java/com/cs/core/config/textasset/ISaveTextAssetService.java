package com.cs.core.config.textasset;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;

public interface ISaveTextAssetService
    extends ISaveConfigService<IKlassSaveModel, IGetKlassEntityWithoutKPModel> {
  
}
