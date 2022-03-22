package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.config.interactor.model.configuration.base.IUpdateDuplicateStatusResponseModel;
import com.cs.core.runtime.interactor.model.propagation.IPropertyInstanceUniquenessEvaluationForPropagationModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IUpdateKlassInstanceDuplicateStatusStrategy extends
    IRuntimeStrategy<IPropertyInstanceUniquenessEvaluationForPropagationModel, IUpdateDuplicateStatusResponseModel> {
  
}
