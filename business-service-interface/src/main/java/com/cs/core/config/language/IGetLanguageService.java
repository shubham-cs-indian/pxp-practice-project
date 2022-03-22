package com.cs.core.config.language;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.language.IGetLanguageModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetLanguageService extends IGetConfigService<IIdParameterModel, IGetLanguageModel> {
  
}
