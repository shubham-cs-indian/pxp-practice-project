package com.cs.core.config.interactor.usecase.asset;

import com.cs.config.interactor.usecase.base.ISaveConfigInteractor;
import com.cs.core.config.interactor.model.asset.IDAMConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.ISaveDAMConfigurationRequestModel;


public interface ISaveDAMConfigurationDetails extends
    ISaveConfigInteractor<ISaveDAMConfigurationRequestModel, IDAMConfigurationDetailsResponseModel> {
  
}
