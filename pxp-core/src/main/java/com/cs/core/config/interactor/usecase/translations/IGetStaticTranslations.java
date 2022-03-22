package com.cs.core.config.interactor.usecase.translations;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsResponseModel;

public interface IGetStaticTranslations
    extends IGetConfigInteractor<IGetTranslationsRequestModel, IGetTranslationsResponseModel> {
  
}
