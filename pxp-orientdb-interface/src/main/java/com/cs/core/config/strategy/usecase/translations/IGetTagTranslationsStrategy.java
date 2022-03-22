package com.cs.core.config.strategy.usecase.translations;

import com.cs.core.config.interactor.model.translations.IGetTagTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.IGetTagTranslationsResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetTagTranslationsStrategy
    extends IConfigStrategy<IGetTagTranslationsRequestModel, IGetTagTranslationsResponseModel> {
  
}
