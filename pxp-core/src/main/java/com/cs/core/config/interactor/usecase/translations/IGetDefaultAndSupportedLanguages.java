package com.cs.core.config.interactor.usecase.translations;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.translations.IDefaultAndSupportingLanguagesModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetDefaultAndSupportedLanguages
    extends IGetConfigInteractor<IIdParameterModel, IDefaultAndSupportingLanguagesModel> {
  
}
