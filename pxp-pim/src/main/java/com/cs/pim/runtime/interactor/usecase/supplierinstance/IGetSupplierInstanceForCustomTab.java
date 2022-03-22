package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetSupplierInstanceForCustomTab
    extends IRuntimeInteractor<IGetInstanceRequestModel, IGetKlassInstanceCustomTabModel> {
  
}
