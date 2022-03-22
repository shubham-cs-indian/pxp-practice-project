package com.cs.core.runtime.interactor.usecase.variant.assetinstance;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkSaveInstanceVariantsModel;
import com.cs.core.runtime.interactor.model.variants.IBulkSaveKlassInstanceVariantsResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IBulkSaveAssetInstanceVariants extends
    IRuntimeInteractor<IBulkSaveInstanceVariantsModel, IBulkSaveKlassInstanceVariantsResponseModel> {
  
}
