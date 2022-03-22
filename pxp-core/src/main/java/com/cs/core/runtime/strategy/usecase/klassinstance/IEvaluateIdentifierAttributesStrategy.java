package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.propagation.IEvaluateIdentifierAttributeResponseModel;
import com.cs.core.runtime.interactor.model.propagation.IEvaluateIdentifierAttributesStrategyModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IEvaluateIdentifierAttributesStrategy extends
    IRuntimeStrategy<IEvaluateIdentifierAttributesStrategyModel, IEvaluateIdentifierAttributeResponseModel> {
  
}
