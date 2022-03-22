package com.cs.core.config.interactor.usecase.language;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetCurrentDefaultLanguage extends IGetConfigInteractor<IModel, IConfigEntityInformationModel> {
  
}
