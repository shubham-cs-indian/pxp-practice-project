package com.cs.core.runtime.strategy.usecase.configdetails;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.collections.IConfigDetailsForBookmarkModel;
import com.cs.core.runtime.interactor.model.collections.IConfigDetailsForBookmarkRequestModel;

public interface IGetConfigDetailsForBookmarkStrategy
    extends IConfigStrategy<IConfigDetailsForBookmarkRequestModel, IConfigDetailsForBookmarkModel> {
  
}
