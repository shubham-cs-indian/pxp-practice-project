package com.cs.core.config.interactor.usecase.asset;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.asset.IDAMConfigurationDetailsResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetDAMConfigurationDetails extends IGetConfigInteractor<IIdParameterModel, IDAMConfigurationDetailsResponseModel> {
  
}
