package com.cs.core.runtime.strategy.usecase.variants;

import com.cs.core.runtime.interactor.model.instance.ISaveStrategyInstanceResponseModel;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantRequestNewStrategyModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface ICreateInstanceVariantStrategy extends
    IRuntimeStrategy<ICreateVariantRequestNewStrategyModel, ISaveStrategyInstanceResponseModel> {
  
}
