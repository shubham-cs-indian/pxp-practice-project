package com.cs.core.config.interactor.usecase.variantcontext;

import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.config.interactor.model.variantcontext.ICreateVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IGetVariantContextModel;

public interface ICreateVariantContext
    extends IConfigInteractor<ICreateVariantContextModel, IGetVariantContextModel> {
  
}
