package com.cs.core.config.supplier;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;

public interface ISaveSupplierService
    extends ISaveConfigService<IKlassSaveModel, IGetKlassEntityWithoutKPModel> {
  
}
