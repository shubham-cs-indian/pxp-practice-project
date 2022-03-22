package com.cs.core.runtime.strategy.usecase.transfer.sourcedestination;

import com.cs.core.config.interactor.model.processdetails.IProcessSourceDestinationDetailsModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICreateSourceDestinationDetailsStrategy
    extends IConfigStrategy<IProcessSourceDestinationDetailsModel, IModel> {
  
}
