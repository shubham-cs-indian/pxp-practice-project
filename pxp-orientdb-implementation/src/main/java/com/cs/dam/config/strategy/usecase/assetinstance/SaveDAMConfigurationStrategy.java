package com.cs.dam.config.strategy.usecase.assetinstance;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.asset.DAMConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.IDAMConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.ISaveDAMConfigurationRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.asset.ISaveDAMConfigurationStrategy;

/**
 * Strategy to save DAM configuration in OrientDB
 * @author pranav.huchche
 *
 */

@Component
public class SaveDAMConfigurationStrategy extends OrientDBBaseStrategy
    implements ISaveDAMConfigurationStrategy {
  
  @Override
  public IDAMConfigurationDetailsResponseModel execute(ISaveDAMConfigurationRequestModel model)
      throws Exception
  {
    return super.execute(OrientDBBaseStrategy.SAVE_DAM_CONFIGURATION, model, DAMConfigurationDetailsResponseModel.class);
  }
  
}
