package com.cs.core.runtime.variant.assetinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantsReturnModel;
import com.cs.core.runtime.interactor.model.variants.IDeleteVariantModel;

public interface IDeleteAssetInstanceVariantsService extends IRuntimeService<IDeleteVariantModel, IBulkDeleteVariantsReturnModel> {
  
}
