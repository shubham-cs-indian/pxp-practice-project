package com.cs.core.config.strategy.usecase.language;

import com.cs.core.config.interactor.model.language.IGetLanguageModel;
import com.cs.core.config.interactor.model.language.ILanguageModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveLanguageStrategy extends IConfigStrategy<ILanguageModel, IGetLanguageModel> {
  
}
