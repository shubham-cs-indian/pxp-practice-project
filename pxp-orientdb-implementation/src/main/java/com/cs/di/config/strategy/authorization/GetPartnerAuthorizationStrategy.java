package com.cs.di.config.strategy.authorization;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.di.config.model.authorization.GetPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.IGetPartnerAuthorizationModel;

@Component("getAuthorizationMappingStrategy")
public class GetPartnerAuthorizationStrategy extends OrientDBBaseStrategy implements IGetPartnerAuthorizationStrategy {
  
  @Override
  public IGetPartnerAuthorizationModel execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_PARTNER_AUTHORIZATION_MAPPING, model, GetPartnerAuthorizationModel.class);
  }
  
}
