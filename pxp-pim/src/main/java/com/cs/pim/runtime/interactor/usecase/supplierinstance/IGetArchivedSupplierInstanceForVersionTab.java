package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForTimeLineModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetArchivedSupplierInstanceForVersionTab extends
IRuntimeInteractor<IGetInstanceRequestModel, IGetKlassInstanceVersionsForTimeLineModel> {
  
}
