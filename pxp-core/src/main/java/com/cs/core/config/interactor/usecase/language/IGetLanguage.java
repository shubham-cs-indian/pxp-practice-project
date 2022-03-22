package com.cs.core.config.interactor.usecase.language;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.language.IGetLanguageModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetLanguage extends IGetConfigInteractor<IIdParameterModel, IGetLanguageModel> {
  
}
