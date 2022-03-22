package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkSaveInstanceVariantsModel;
import com.cs.core.runtime.interactor.model.variants.IBulkSaveKlassInstanceVariantsResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IBulkSaveSupplierInstanceVariants extends
    IRuntimeInteractor<IBulkSaveInstanceVariantsModel, IBulkSaveKlassInstanceVariantsResponseModel> {
  
}
