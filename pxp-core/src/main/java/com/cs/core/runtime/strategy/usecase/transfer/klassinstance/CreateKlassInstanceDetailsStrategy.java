package com.cs.core.runtime.strategy.usecase.transfer.klassinstance;

import com.cs.core.config.interactor.model.processdetails.IProcessKlassInstanceStatusModel;
import com.cs.core.runtime.interactor.constants.application.TransferConstants;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.strategy.db.BasePostgresStrategy;
import com.cs.core.runtime.strategy.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateKlassInstanceDetailsStrategy extends BasePostgresStrategy
    implements ICreateKlassInstanceDetailsStrategy {
  
  @Autowired
  DbUtils dbUtils;
  
  @Override
  public IModel execute(IProcessKlassInstanceStatusModel model) throws Exception
  {
    dbUtils.insertQuery(TransferConstants.KLASS_INSTANCE_STATUS_TABLE,
        dbUtils.getModelIntoMap(model));
    return null;
  }
}
