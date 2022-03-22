package com.cs.core.config.interactor.usecase.translations;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsResponseModel;

public interface ISaveStaticTranslations
    extends ISaveConfigInteractor<ISaveTranslationsRequestModel, ISaveTranslationsResponseModel> {
  
}
