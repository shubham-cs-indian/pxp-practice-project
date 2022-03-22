package com.cs.core.config.strategy.usecase.configdata;

import com.cs.core.config.interactor.model.tag.IGetTagTagValuesRequestModel;
import com.cs.core.config.interactor.model.tag.IGetTagTagValuesResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetTagTagValuesStrategy
    extends IConfigStrategy<IGetTagTagValuesRequestModel, IGetTagTagValuesResponseModel> {
  
}
