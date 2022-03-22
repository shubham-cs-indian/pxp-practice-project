package com.cs.core.config.interactor.usecase.language;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.language.IGetLanguagesRequestModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesResponseModel;

public interface IGetLanguages
    extends IGetConfigInteractor<IGetLanguagesRequestModel, IGetLanguagesResponseModel> {
  
}
