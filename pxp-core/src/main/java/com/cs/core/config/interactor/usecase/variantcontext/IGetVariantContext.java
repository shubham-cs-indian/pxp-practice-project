package com.cs.core.config.interactor.usecase.variantcontext;

import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.config.interactor.model.variantcontext.IGetVariantContextModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetVariantContext
    extends IConfigInteractor<IIdParameterModel, IGetVariantContextModel> {
  
}
