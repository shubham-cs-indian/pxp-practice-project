package com.cs.core.config.klass;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;

public interface ISaveKlassService
    extends ISaveConfigService<IKlassSaveModel, IGetKlassEntityWithoutKPModel> {
  
}
