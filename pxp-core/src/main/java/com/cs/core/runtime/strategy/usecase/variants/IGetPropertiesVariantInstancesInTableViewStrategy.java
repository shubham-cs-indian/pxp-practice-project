package com.cs.core.runtime.strategy.usecase.variants;

import com.cs.core.runtime.interactor.model.variants.IGetPropertiesVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.model.variants.IGetPropertiesVariantInstancesInTableViewRequestStrategyModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetPropertiesVariantInstancesInTableViewStrategy extends
    IRuntimeStrategy<IGetPropertiesVariantInstancesInTableViewRequestStrategyModel, IGetPropertiesVariantInstancesInTableViewModel> {
  
}
