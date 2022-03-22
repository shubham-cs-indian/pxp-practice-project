package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.config.interactor.model.context.IContextualValueInheritancePropagationModel;
import com.cs.core.config.interactor.model.klass.IContextKlassSavePropertiesToInheritModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetContextualDataTransferForValueInheritanceStrategy extends
    IRuntimeStrategy<IContextKlassSavePropertiesToInheritModel, IContextualValueInheritancePropagationModel> {
  
}
