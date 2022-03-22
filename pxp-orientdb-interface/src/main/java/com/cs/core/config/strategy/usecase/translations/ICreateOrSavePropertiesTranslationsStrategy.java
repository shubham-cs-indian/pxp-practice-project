package com.cs.core.config.strategy.usecase.translations;

import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateOrSavePropertiesTranslationsStrategy
    extends IConfigStrategy<ISaveTranslationsRequestModel, ISaveTranslationsResponseModel> {
  
}
