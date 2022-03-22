package com.cs.core.config.language;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetCurrentDefaultLanguageService extends IGetConfigService<IModel, IConfigEntityInformationModel> {
  
}
