package com.cs.di.config.strategy.authorization;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.di.config.model.authorization.GetPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.IPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.IGetPartnerAuthorizationModel;

@Component("createAuthorizationMappingStrategy")
public class CreatePartnerAuthorizationStrategy extends OrientDBBaseStrategy implements ICreatePartnerAuthorizationStrategy {
  
  
  @Override
  public IGetPartnerAuthorizationModel execute(IPartnerAuthorizationModel model) throws Exception
  {
    return execute(CREATE_PARTNER_AUTHORIZATION, model, GetPartnerAuthorizationModel.class);
  }
}
