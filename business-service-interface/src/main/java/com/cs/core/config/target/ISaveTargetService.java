package com.cs.core.config.target;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;

public interface ISaveTargetService
    extends ISaveConfigService<IKlassSaveModel, IGetKlassEntityWithoutKPModel> {
  
}
