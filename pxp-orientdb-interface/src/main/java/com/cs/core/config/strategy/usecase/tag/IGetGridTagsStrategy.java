package com.cs.core.config.strategy.usecase.tag;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.tag.IGetTagGridResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetGridTagsStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetTagGridResponseModel> {
  
}
