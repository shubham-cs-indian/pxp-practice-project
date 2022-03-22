package com.cs.core.config.interactor.usecase.language;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.language.ICreateLanguageModel;
import com.cs.core.config.interactor.model.language.IGetLanguageModel;

public interface ICreateLanguage extends ICreateConfigInteractor<ICreateLanguageModel, IGetLanguageModel> {
  
}
