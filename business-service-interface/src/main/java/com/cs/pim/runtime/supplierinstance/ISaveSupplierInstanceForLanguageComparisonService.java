package com.cs.pim.runtime.supplierinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.supplierinstance.ISupplierInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;

public interface ISaveSupplierInstanceForLanguageComparisonService
    extends IRuntimeService<ISupplierInstanceSaveModel, IGetKlassInstanceModel> {
  
}
