package com.cs.core.config.language;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.language.IGetLanguageModel;
import com.cs.core.config.interactor.model.language.ILanguageModel;

public interface ISaveLanguageService extends ISaveConfigService<ILanguageModel, IGetLanguageModel> {
  
}
