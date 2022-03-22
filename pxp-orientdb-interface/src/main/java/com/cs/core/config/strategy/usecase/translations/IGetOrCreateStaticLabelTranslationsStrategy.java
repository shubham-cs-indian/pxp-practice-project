package com.cs.core.config.strategy.usecase.translations;

import com.cs.core.config.interactor.model.translations.ICreateStaticLabelTranslationsRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetOrCreateStaticLabelTranslationsStrategy
    extends IConfigStrategy<ICreateStaticLabelTranslationsRequestModel, IModel> {
  
}
