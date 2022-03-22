package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.variants.IRestoreVariantRequestModel;
import com.cs.core.runtime.interactor.model.versionrollback.IRollbackInstanceStrategyResponseModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IRestoreKlassInstanceStrategy
    extends IRuntimeStrategy<IRestoreVariantRequestModel, IRollbackInstanceStrategyResponseModel> {
  
}
