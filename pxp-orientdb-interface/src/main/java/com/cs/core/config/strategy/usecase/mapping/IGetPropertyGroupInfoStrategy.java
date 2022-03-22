package com.cs.core.config.strategy.usecase.mapping;

import com.cs.core.config.interactor.model.mapping.IGetPropertyGroupInfoRequestModel;
import com.cs.core.config.interactor.model.mapping.IGetPropertyGroupInfoResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetPropertyGroupInfoStrategy
    extends IConfigStrategy<IGetPropertyGroupInfoRequestModel, IGetPropertyGroupInfoResponseModel> {
  
}
