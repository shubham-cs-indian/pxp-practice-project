package com.cs.core.runtime.strategy.usecase.goldenrecord;

import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenRequestModel;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetConfigDetailsForGetGoldenRecordFilterChildrenStrategy 
    extends IConfigStrategy<IConfigDetailsForGetFilterChildrenRequestModel, IConfigDetailsForGetFilterChildrenResponseModel> {
  
}
