package com.cs.core.config.strategy.usecase.language;

import com.cs.core.config.interactor.model.language.IGetLanguageModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetLanguageByLocaleIdStrategy
    extends IConfigStrategy<IIdParameterModel, IGetLanguageModel> {
  
}
