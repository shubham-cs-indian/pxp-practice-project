package com.cs.core.config.strategy.usecase.instancetree;

import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenRequestModel;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetConfigDetailsForGetFilterChildrenStrategy extends
    IConfigStrategy<IConfigDetailsForGetFilterChildrenRequestModel, IConfigDetailsForGetFilterChildrenResponseModel> {
  
}
