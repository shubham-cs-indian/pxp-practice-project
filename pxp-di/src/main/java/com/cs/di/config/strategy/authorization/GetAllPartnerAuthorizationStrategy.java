package com.cs.di.config.strategy.authorization;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.di.config.model.authorization.GetAllPartnerAuthorizationModel;
import com.cs.di.config.model.authorization.IGetAllPartnerAuthorizationModel;

@Component("getAllAuthorizationMappingStrategy")
public class GetAllPartnerAuthorizationStrategy extends OrientDBBaseStrategy
    implements IGetAllPartnerAuthorizationStrategy {
  
  @Override
  public IGetAllPartnerAuthorizationModel execute(IConfigGetAllRequestModel model) throws Exception
  {
    return execute(GET_ALL_PARTNER_AUTHORIZATION_MAPPINGS, model, GetAllPartnerAuthorizationModel.class);
  }
}
