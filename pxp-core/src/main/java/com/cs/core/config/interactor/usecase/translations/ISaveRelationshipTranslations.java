package com.cs.core.config.interactor.usecase.translations;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.translations.ISaveRelationshipTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.ISaveRelationshipTranslationsResponseModel;

public interface ISaveRelationshipTranslations extends
    ISaveConfigInteractor<ISaveRelationshipTranslationsRequestModel, ISaveRelationshipTranslationsResponseModel> {
  
}
