package com.cs.core.config.interactor.usecase.entityconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;

@Service
public class GetRelationshipEntityConfiguration extends AbstractGetConfigEntityConfiguration
    implements IGetEntityConfiguration {
  
  @Autowired
  protected IGetEntityConfigurationStrategy getRelationshipEntityConfigurationStrategy;
  
  @Override
  protected IGetEntityConfigurationResponseModel executeInternal(
      IGetEntityConfigurationRequestModel model) throws Exception
  {
    return getRelationshipEntityConfigurationStrategy.execute(model);
  }
  
}
