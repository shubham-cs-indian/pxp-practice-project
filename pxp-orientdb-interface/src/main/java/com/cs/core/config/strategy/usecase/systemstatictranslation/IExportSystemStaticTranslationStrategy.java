package com.cs.core.config.strategy.usecase.systemstatictranslation;

import com.cs.core.config.interactor.model.hidden.IGetTranslationsResponseHiddenModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IExportSystemStaticTranslationStrategy
    extends IConfigStrategy<IGetTranslationsRequestModel, IGetTranslationsResponseHiddenModel> {
  
}
