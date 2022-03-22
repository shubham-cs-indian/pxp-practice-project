package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import com.cs.core.runtime.interactor.model.supplierinstance.ISupplierInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ISaveSupplierInstanceForOverviewTab
    extends IRuntimeInteractor<ISupplierInstanceSaveModel, IGetKlassInstanceModel> {
  
}
