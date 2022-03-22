package com.cs.core.runtime.strategy.usecase.transfer.variant;

import com.cs.core.config.interactor.model.processdetails.IProcessVariantStatusModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICreateVariantDetailsStrategy
    extends IConfigStrategy<IProcessVariantStatusModel, IModel> {
  
}
