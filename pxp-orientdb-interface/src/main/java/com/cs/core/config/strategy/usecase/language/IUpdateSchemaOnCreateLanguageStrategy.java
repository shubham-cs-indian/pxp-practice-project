package com.cs.core.config.strategy.usecase.language;

import com.cs.core.config.interactor.model.language.IUpdateSchemaOnLangaugeCreateModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IUpdateSchemaOnCreateLanguageStrategy
    extends IConfigStrategy<IUpdateSchemaOnLangaugeCreateModel, IIdsListParameterModel> {
  
}
