package com.cs.core.config.language;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.language.ICreateLanguageModel;
import com.cs.core.config.interactor.model.language.IGetLanguageModel;

public interface ICreateLanguageService extends ICreateConfigService<ICreateLanguageModel, IGetLanguageModel> {
  
}
