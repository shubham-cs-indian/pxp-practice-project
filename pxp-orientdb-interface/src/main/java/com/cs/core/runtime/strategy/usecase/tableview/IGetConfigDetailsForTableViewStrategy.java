package com.cs.core.runtime.strategy.usecase.tableview;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetConfigDetailsForGetNewInstanceTreeRequestModel;

public interface IGetConfigDetailsForTableViewStrategy
  extends IConfigStrategy<IGetConfigDetailsForGetNewInstanceTreeRequestModel, IConfigDetailsForGetNewInstanceTreeModel> {
  
}
