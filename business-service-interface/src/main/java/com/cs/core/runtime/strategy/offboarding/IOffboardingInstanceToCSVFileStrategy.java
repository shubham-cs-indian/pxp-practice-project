package com.cs.core.runtime.strategy.offboarding;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.IWriteInstancesToCSVFileModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IOffboardingInstanceToCSVFileStrategy
    extends IRuntimeStrategy<IWriteInstancesToCSVFileModel, IModel> {
  
}
