package com.cs.core.runtime.strategy.usecase.transfer.processstatus;

import com.cs.core.config.interactor.model.processdetails.IProcessStatusDetailsModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetProcessStatusByIdStrategy
    extends IConfigStrategy<IProcessStatusDetailsModel, IProcessStatusDetailsModel> {
  
}
