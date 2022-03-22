package com.cs.core.config.strategy.usecase.language;

import com.cs.core.config.interactor.model.language.IGetOrCreateLanguageModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetOrCreateLanguageStrategy
    extends IConfigStrategy<IGetOrCreateLanguageModel, IIdParameterModel> {
  
}
