package com.cs.core.config.strategy.usecase.duplicatecode;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.klassinstance.IConflictSourcesRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetConflictSourcesInformationModel;

public interface IGetConflictSourcesInformationConfigStrategy
    extends IConfigStrategy<IConflictSourcesRequestModel, IGetConflictSourcesInformationModel> {
  
}
