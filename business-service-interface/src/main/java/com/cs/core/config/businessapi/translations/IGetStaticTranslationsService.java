package com.cs.core.config.businessapi.translations;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsResponseModel;

public interface IGetStaticTranslationsService extends IGetConfigService<IGetTranslationsRequestModel, IGetTranslationsResponseModel> {
  
}
