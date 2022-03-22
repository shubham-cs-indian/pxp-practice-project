package com.cs.core.runtime.interactor.usecase.fileinstance;

import com.cs.core.config.interactor.model.mapping.IRuntimeMappingModel;
import com.cs.core.runtime.interactor.model.fileinstance.ICreateOnboardingFileInstanceModel;
import com.cs.core.runtime.interactor.usecase.cammunda.IComponent;

public interface ICreateOnboardingFileInstance
    extends IComponent<ICreateOnboardingFileInstanceModel, IRuntimeMappingModel> {
}
