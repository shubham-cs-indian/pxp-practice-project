package com.cs.core.runtime.strategy.usecase.transfer.variant;

import com.cs.core.config.interactor.model.processdetails.IProcessVariantStatusModel;
import com.cs.core.runtime.interactor.constants.application.TransferConstants;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.strategy.db.BasePostgresStrategy;
import com.cs.core.runtime.strategy.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateAttributeVariantDetailsStrategy extends BasePostgresStrategy
    implements ICreateAttributeVariantDetailsStrategy {
  
  @Autowired
  DbUtils dbUtils;
  
  @Override
  public IModel execute(IProcessVariantStatusModel model) throws Exception
  {
    dbUtils.insertQuery(TransferConstants.ATTRIBUTE_VARIANT_STATUS_TABLE,
        dbUtils.getModelIntoMap(model));
    return null;
  }
}
