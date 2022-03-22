package com.cs.core.config.businessapi.translations;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.translations.IDefaultAndSupportingLanguagesModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetDefaultAndSupportedLanguagesService extends IGetConfigService<IIdParameterModel, IDefaultAndSupportingLanguagesModel> {
  
}
