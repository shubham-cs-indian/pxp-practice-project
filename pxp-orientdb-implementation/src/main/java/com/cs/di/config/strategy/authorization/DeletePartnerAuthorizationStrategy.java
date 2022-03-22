package com.cs.di.config.strategy.authorization;


import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Component("deleteAuthorizationMappingStrategy")
public class DeletePartnerAuthorizationStrategy extends OrientDBBaseStrategy implements IDeletePartnerAuthorizationStrategy {
    
  @Override
  public IBulkDeleteReturnModel execute(IIdsListParameterModel model) throws Exception
  {
      return execute(DELETE_PARTNER_AUTHORIZATION_MAPPING, model, BulkDeleteReturnModel.class);
  }
}
