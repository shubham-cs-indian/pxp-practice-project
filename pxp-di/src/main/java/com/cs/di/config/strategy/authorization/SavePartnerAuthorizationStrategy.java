package com.cs.di.config.strategy.authorization;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.di.config.model.authorization.GetPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.IGetPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.ISavePartnerAuthorizationModel;

@Component("saveAuthorizationMappingStrategy")
public class SavePartnerAuthorizationStrategy extends OrientDBBaseStrategy implements ISavePartnerAuthorizationStrategy {
  
  @Override
  public IGetPartnerAuthorizationModel execute(ISavePartnerAuthorizationModel model) throws Exception
  {
    return execute(SAVE_PARTNER_AUTHORIZATION_MAPPING, model, GetPartnerAuthorizationModel.class);
  }
  
}
