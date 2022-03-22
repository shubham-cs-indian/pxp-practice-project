package com.cs.core.runtime.strategy.usecase.asset;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.asset.DAMConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.IDAMConfigurationDetailsResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

/**
 * Get DAM configuration strategy from OrientDB.
 * @author pranav.huchche
 *
 */
@Component
public class GetDAMConfigurationDetailsStrategy extends OrientDBBaseStrategy
    implements IGetDAMConfigurationDetailsStrategy {
  
  @Override
  public IDAMConfigurationDetailsResponseModel execute(IIdParameterModel model) throws Exception
  {
    return super.execute(OrientDBBaseStrategy.FETCH_DAM_CONFIGURATION_DETAILS, model, DAMConfigurationDetailsResponseModel.class);
  }
}
