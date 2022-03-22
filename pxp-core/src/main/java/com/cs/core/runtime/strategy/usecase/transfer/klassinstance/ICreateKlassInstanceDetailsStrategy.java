package com.cs.core.runtime.strategy.usecase.transfer.klassinstance;

import com.cs.core.config.interactor.model.processdetails.IProcessKlassInstanceStatusModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICreateKlassInstanceDetailsStrategy
    extends IConfigStrategy<IProcessKlassInstanceStatusModel, IModel> {
  
}
