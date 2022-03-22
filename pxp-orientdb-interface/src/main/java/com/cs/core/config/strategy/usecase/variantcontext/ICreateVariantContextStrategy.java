package com.cs.core.config.strategy.usecase.variantcontext;

import com.cs.core.config.interactor.model.variantcontext.ICreateVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IGetVariantContextModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateVariantContextStrategy
    extends IConfigStrategy<ICreateVariantContextModel, IGetVariantContextModel> {
  
}
