package com.cs.core.runtime.strategy.usecase.transfer.relationship;

import com.cs.core.config.interactor.model.processdetails.IProcessRelationshipDetailsModel;
import com.cs.core.runtime.interactor.constants.application.TransferConstants;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.strategy.db.BasePostgresStrategy;
import com.cs.core.runtime.strategy.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateNatureRealtionshipDetailsStrategy extends BasePostgresStrategy
    implements ICreateNatureRelationshipDetailsStrategy {
  
  @Autowired
  DbUtils dbUtils;
  
  @Override
  public IModel execute(IProcessRelationshipDetailsModel model) throws Exception
  {
    dbUtils.insertQuery(TransferConstants.NATURE_RELATIONSHIP_STATUS_TABLE,
        dbUtils.getModelIntoMap(model));
    return null;
  }
}
