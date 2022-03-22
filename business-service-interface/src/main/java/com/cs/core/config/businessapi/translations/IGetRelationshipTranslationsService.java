package com.cs.core.config.businessapi.translations;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.translations.IGetRelationshipTranslationsResponseModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;

public interface IGetRelationshipTranslationsService
    extends IGetConfigService<IGetTranslationsRequestModel, IGetRelationshipTranslationsResponseModel> {
  
}
