package com.cs.core.config.interactor.usecase.language;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.language.IGetLanguageModel;
import com.cs.core.config.interactor.model.language.ILanguageModel;

public interface ISaveLanguage extends ISaveConfigInteractor<ILanguageModel, IGetLanguageModel> {
  
}
