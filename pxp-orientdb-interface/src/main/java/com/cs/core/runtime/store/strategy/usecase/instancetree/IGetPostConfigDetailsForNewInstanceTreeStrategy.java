package com.cs.core.runtime.store.strategy.usecase.instancetree;

import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetPostConfigDetailsForNewInstanceTreeModel;

public interface IGetPostConfigDetailsForNewInstanceTreeStrategy
    extends IConfigStrategy<IGetPostConfigDetailsRequestModel, IGetPostConfigDetailsForNewInstanceTreeModel> {
  
}
