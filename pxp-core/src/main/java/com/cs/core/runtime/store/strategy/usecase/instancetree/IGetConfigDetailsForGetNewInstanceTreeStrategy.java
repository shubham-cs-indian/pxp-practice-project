package com.cs.core.runtime.store.strategy.usecase.instancetree;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetConfigDetailsForGetNewInstanceTreeRequestModel;

public interface IGetConfigDetailsForGetNewInstanceTreeStrategy 
  extends IConfigStrategy<IGetConfigDetailsForGetNewInstanceTreeRequestModel, IConfigDetailsForGetNewInstanceTreeModel> {
  
}
