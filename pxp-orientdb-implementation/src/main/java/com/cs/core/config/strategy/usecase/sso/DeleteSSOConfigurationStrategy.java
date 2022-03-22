package com.cs.core.config.strategy.usecase.sso;

import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.stereotype.Component;

@Component
public class DeleteSSOConfigurationStrategy extends OrientDBBaseStrategy
    implements IDeleteSSOConfigurationStrategy {
  
  @Override
  public IBulkDeleteReturnModel execute(IIdsListParameterModel model) throws Exception
  {
    return execute(DELETE_SSO_CONFIGURATION, model, BulkDeleteReturnModel.class);
  }
}
