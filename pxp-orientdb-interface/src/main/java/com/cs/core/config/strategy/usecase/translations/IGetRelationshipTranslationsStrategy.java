package com.cs.core.config.strategy.usecase.translations;

import com.cs.core.config.interactor.model.translations.IGetRelationshipTranslationsResponseModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetRelationshipTranslationsStrategy extends
    IConfigStrategy<IGetTranslationsRequestModel, IGetRelationshipTranslationsResponseModel> {
  
}
