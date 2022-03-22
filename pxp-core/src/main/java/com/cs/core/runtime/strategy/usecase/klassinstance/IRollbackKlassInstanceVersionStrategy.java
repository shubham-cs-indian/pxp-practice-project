package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.versionrollback.IRollbackInstanceStrategyRequestModel;
import com.cs.core.runtime.interactor.model.versionrollback.IRollbackInstanceStrategyResponseModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IRollbackKlassInstanceVersionStrategy extends
    IRuntimeStrategy<IRollbackInstanceStrategyRequestModel, IRollbackInstanceStrategyResponseModel> {
  
}
