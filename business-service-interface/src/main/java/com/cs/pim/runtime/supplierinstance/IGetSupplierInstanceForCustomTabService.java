package com.cs.pim.runtime.supplierinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;

public interface IGetSupplierInstanceForCustomTabService
    extends IRuntimeService<IGetInstanceRequestModel, IGetKlassInstanceCustomTabModel> {
  
}
