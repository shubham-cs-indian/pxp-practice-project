package com.cs.core.config.strategy.usecase.translations;

import com.cs.core.config.interactor.model.translations.ISaveRelationshipTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.ISaveRelationshipTranslationsResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveRelationshipTranslationsStrategy extends
    IConfigStrategy<ISaveRelationshipTranslationsRequestModel, ISaveRelationshipTranslationsResponseModel> {
  
}
