package com.cs.core.config.strategy.usecase.organization;

import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.stereotype.Component;

@Component
public class DeleteOrganizationStrategy extends OrientDBBaseStrategy
    implements IDeleteOrganizationStrategy {
  
  @Override
  public IBulkDeleteReturnModel execute(IIdsListParameterModel model) throws Exception
  {
    return execute(DELETE_ORGANIZATION, model, BulkDeleteReturnModel.class);
  }
}
