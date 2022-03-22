package com.cs.core.config.interactor.usecase.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.asset.IDAMConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.ISaveDAMConfigurationRequestModel;
import com.cs.core.config.strategy.usecase.asset.ISaveDAMConfigurationStrategy;

/**
 * This is service layer to save DAM configuration.
 * @author pranav.huchche
 *
 */

@Service
public class SaveDAMConfigurationDetails extends AbstractSaveConfigInteractor<ISaveDAMConfigurationRequestModel, IDAMConfigurationDetailsResponseModel>
  implements ISaveDAMConfigurationDetails 
  {
  
  @Autowired
  ISaveDAMConfigurationStrategy saveDAMConfigurationStrategy;
  
  @Override
  protected IDAMConfigurationDetailsResponseModel executeInternal(
      ISaveDAMConfigurationRequestModel model) throws Exception
  {
    return saveDAMConfigurationStrategy.execute(model);
  }
  
}
