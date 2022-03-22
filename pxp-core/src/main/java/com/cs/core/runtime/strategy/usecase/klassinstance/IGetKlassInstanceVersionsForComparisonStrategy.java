package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForComparisonModel;
import com.cs.core.runtime.interactor.model.version.IKlassInstanceVersionsComparisonModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetKlassInstanceVersionsForComparisonStrategy extends
    IRuntimeStrategy<IKlassInstanceVersionsComparisonModel, IGetKlassInstanceVersionsForComparisonModel> {
  
}
