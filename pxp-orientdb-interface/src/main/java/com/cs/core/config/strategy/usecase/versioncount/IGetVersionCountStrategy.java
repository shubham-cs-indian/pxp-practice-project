package com.cs.core.config.strategy.usecase.versioncount;

import com.cs.core.config.interactor.model.version.IVersionCountModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetVersionCountStrategy
    extends IConfigStrategy<IIdsListParameterModel, IVersionCountModel> {
  
}
