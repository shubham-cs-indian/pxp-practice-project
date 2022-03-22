package com.cs.core.config.interactor.usecase.translations;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.translations.IGetRelationshipTranslationsResponseModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;

public interface IGetRelationshipTranslations extends
    IGetConfigInteractor<IGetTranslationsRequestModel, IGetRelationshipTranslationsResponseModel> {
  
}
