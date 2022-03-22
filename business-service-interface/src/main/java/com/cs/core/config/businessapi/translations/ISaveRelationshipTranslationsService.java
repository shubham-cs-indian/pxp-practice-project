package com.cs.core.config.businessapi.translations;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.translations.ISaveRelationshipTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.ISaveRelationshipTranslationsResponseModel;

public interface ISaveRelationshipTranslationsService
    extends ISaveConfigService<ISaveRelationshipTranslationsRequestModel, ISaveRelationshipTranslationsResponseModel> {
  
}
