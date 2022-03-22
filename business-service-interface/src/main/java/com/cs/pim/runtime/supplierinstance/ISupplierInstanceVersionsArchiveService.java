package com.cs.pim.runtime.supplierinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsModel;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;

public interface ISupplierInstanceVersionsArchiveService extends
    IRuntimeService<IMoveKlassInstanceVersionsModel, IMoveKlassInstanceVersionsSuccessModel> {
  
}