package com.cs.core.runtime.store.strategy.usecase.instancetree;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetRQFilterChildrenRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetRQFilterChildrenResponseModel;

public interface IGetConfigDetailsForGetRQFilterChildrenStrategy 
  extends IConfigStrategy<IConfigDetailsForGetRQFilterChildrenRequestModel, IConfigDetailsForGetRQFilterChildrenResponseModel> {
  
}
