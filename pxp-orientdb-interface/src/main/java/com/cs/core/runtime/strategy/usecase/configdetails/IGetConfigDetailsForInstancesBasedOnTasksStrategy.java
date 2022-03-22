package com.cs.core.runtime.strategy.usecase.configdetails;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceBasedOnTaskGetModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetConfigDetailsForInstancesBasedOnTasksStrategy
    extends IConfigStrategy<IIdParameterModel, IConfigDetailsForInstanceBasedOnTaskGetModel> {
  
}
