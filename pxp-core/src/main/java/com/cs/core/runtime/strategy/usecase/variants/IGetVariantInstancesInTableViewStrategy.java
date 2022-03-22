package com.cs.core.runtime.strategy.usecase.variants;

import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstancesInTableViewRequestStrategyModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetVariantInstancesInTableViewStrategy extends
    IRuntimeStrategy<IGetVariantInstancesInTableViewRequestStrategyModel, IGetVariantInstancesInTableViewModel> {
  
}
