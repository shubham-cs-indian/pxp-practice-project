package com.cs.core.config.language;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.language.IGetLanguagesRequestModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesResponseModel;

public interface IGetLanguagesService extends IGetConfigService<IGetLanguagesRequestModel, IGetLanguagesResponseModel> {
  
}
