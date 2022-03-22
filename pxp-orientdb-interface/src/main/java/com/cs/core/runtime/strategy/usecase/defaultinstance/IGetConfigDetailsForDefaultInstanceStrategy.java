package com.cs.core.runtime.strategy.usecase.defaultinstance;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;

public interface IGetConfigDetailsForDefaultInstanceStrategy
    extends IConfigStrategy<IMulticlassificationRequestModel, IGetConfigDetailsForCustomTabModel> {
  
}
