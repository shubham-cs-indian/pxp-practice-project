package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.variants.IConfigDetailsForGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.model.variants.IConfigDetailsForGetVariantInstancesInTableViewRequestModel;

public interface IGetConfigDetailsForGetVariantInstancesInTableViewStrategy extends
    IConfigStrategy<IConfigDetailsForGetVariantInstancesInTableViewRequestModel, IConfigDetailsForGetVariantInstancesInTableViewModel> {
}
