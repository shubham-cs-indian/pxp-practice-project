package com.cs.core.runtime.strategy.offboarding;

import com.cs.core.runtime.interactor.model.fileinstance.ICreateOnboardingFileInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IWriteInstancesToXLSXFileModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IOffboardingInstancesToXLSXStrategy
    extends IRuntimeStrategy<IWriteInstancesToXLSXFileModel, ICreateOnboardingFileInstanceModel> {
  
}
