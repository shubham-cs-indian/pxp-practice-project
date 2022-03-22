package com.cs.core.config.interactor.usecase.entityconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;

/**
 * This is  icon entityUsage  Intractor.
 * @author jamil.ahmad
 *
 */
@Service
public class GetIconEntityConfiguration extends AbstractGetConfigEntityConfiguration implements IGetEntityConfiguration {
  
  @Autowired
  protected IGetEntityConfigurationStrategy getIconEntityConfigurationStrategy;
  
  @Override
  protected IGetEntityConfigurationResponseModel executeInternal(IGetEntityConfigurationRequestModel model) throws Exception
  {
    return getIconEntityConfigurationStrategy.execute(model);
  }
  
}
