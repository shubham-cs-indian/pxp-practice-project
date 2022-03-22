package com.cs.core.config.interactor.usecase.variantcontext;

import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.variantcontext.IGetAllVariantContextsResponseModel;

public interface IGetAllVariantContext
    extends IConfigInteractor<IConfigGetAllRequestModel, IGetAllVariantContextsResponseModel> {
  
}
