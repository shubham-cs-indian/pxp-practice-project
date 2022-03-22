package com.cs.core.runtime.strategy.usecase.transfer.processstatus;

import com.cs.core.config.interactor.model.processdetails.IProcessStatusDetailsModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICreateProcessStatusStrategy
    extends IConfigStrategy<IProcessStatusDetailsModel, IModel> {
  
}
