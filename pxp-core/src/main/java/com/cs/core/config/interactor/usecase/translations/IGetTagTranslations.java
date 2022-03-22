package com.cs.core.config.interactor.usecase.translations;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.translations.IGetTagTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.IGetTagTranslationsResponseModel;

public interface IGetTagTranslations
    extends IGetConfigInteractor<IGetTagTranslationsRequestModel, IGetTagTranslationsResponseModel> {
  
}
