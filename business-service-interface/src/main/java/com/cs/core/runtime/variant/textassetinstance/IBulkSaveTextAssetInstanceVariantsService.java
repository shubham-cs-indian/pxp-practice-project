package com.cs.core.runtime.variant.textassetinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkSaveInstanceVariantsModel;
import com.cs.core.runtime.interactor.model.variants.IBulkSaveKlassInstanceVariantsResponseModel;

public interface IBulkSaveTextAssetInstanceVariantsService
    extends IRuntimeService<IBulkSaveInstanceVariantsModel, IBulkSaveKlassInstanceVariantsResponseModel> {
  
}
