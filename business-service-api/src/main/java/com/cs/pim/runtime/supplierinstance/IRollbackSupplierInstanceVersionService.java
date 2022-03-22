package com.cs.pim.runtime.supplierinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.versionrollback.IKlassInstanceVersionRollbackModel;

public interface IRollbackSupplierInstanceVersionService  extends IRuntimeService<IKlassInstanceVersionRollbackModel,IGetKlassInstanceModel>{
  
}

