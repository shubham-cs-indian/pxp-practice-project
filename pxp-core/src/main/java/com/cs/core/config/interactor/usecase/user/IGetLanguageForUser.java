package com.cs.core.config.interactor.usecase.user;

import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.config.interactor.model.user.IUserLanguageModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetLanguageForUser extends IConfigInteractor<IIdParameterModel, IUserLanguageModel> {
  
}
