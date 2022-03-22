package com.cs.core.runtime.strategy.usecase.fileinstance;

import com.cs.core.runtime.interactor.model.fileinstance.IOnboardingFileInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceWithDataRuleModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface ICreateOnboardingFileInstanceStrategy
    extends IRuntimeStrategy<IKlassInstanceWithDataRuleModel, IOnboardingFileInstanceModel> {
  
}
