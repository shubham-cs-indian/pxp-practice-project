package com.cs.core.runtime.strategy.usecase.transfer.processstatus;

import com.cs.core.config.interactor.model.processdetails.IProcessStatusDetailsModel;
import com.cs.core.runtime.interactor.constants.application.TransferConstants;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.strategy.db.BasePostgresStrategy;
import com.cs.core.runtime.strategy.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateProcessStatusStrategy extends BasePostgresStrategy
    implements ICreateProcessStatusStrategy {
  
  @Autowired
  DbUtils dbUtils;
  
  @Override
  public IModel execute(IProcessStatusDetailsModel model) throws Exception
  {
    dbUtils.insertQuery(TransferConstants.PROCESS_STATUS_TABLE, dbUtils.getModelIntoMap(model));
    return null;
  }
}
