package com.cs.core.runtime.strategy.usecase.transfer.sourcedestination;

import com.cs.core.config.interactor.model.processdetails.IProcessSourceDestinationDetailsModel;
import com.cs.core.runtime.interactor.constants.application.TransferConstants;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.strategy.db.BasePostgresStrategy;
import com.cs.core.runtime.strategy.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateSourceDestinationDetailsStrategy extends BasePostgresStrategy
    implements ICreateSourceDestinationDetailsStrategy {
  
  @Autowired
  DbUtils dbUtils;
  
  @Override
  public IModel execute(IProcessSourceDestinationDetailsModel model) throws Exception
  {
    dbUtils.insertQuery(TransferConstants.SOURCE_DESTINATION_TABLE, dbUtils.getModelIntoMap(model));
    return null;
  }
}
