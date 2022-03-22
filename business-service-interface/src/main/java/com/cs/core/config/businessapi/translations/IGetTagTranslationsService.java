package com.cs.core.config.businessapi.translations;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.translations.IGetTagTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.IGetTagTranslationsResponseModel;

public interface IGetTagTranslationsService extends IGetConfigService<IGetTagTranslationsRequestModel, IGetTagTranslationsResponseModel> {
  
}
