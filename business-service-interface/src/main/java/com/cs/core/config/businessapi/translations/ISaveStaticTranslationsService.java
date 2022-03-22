package com.cs.core.config.businessapi.translations;

import com.cs.config.businessapi.base.ISaveConfigService;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsResponseModel;

public interface ISaveStaticTranslationsService extends ISaveConfigService<ISaveTranslationsRequestModel, ISaveTranslationsResponseModel> {
  
}
