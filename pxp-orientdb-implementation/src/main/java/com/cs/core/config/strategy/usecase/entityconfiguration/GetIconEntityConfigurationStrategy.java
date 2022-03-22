package com.cs.core.config.strategy.usecase.entityconfiguration;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.GetEntityConfigurationResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

/**
 * This is  icon entityUsage  Strategy.
 * @author jamil.ahmad
 *
 */
@Component
public class GetIconEntityConfigurationStrategy extends OrientDBBaseStrategy implements IGetEntityConfigurationStrategy {
  
  @Override
  public IGetEntityConfigurationResponseModel execute(IGetEntityConfigurationRequestModel model) throws Exception
  {
    return execute(GET_ICON_ENTITY_CONFIGURATION, model, GetEntityConfigurationResponseModel.class);
  }
  
}
