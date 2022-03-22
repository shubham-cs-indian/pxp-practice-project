package com.cs.core.runtime.strategy.usecase.transfer.relationship;

import com.cs.core.config.interactor.model.processdetails.IProcessRelationshipDetailsModel;
import com.cs.core.runtime.interactor.constants.application.TransferConstants;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.strategy.db.BasePostgresStrategy;
import com.cs.core.runtime.strategy.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SaveNatureRealtionshipDetailsStrategy extends BasePostgresStrategy
    implements ISaveNatureRelationshipDetailsStrategy {
  
  @Autowired
  DbUtils dbUtils;
  
  @Override
  public IModel execute(IProcessRelationshipDetailsModel model) throws Exception
  {
    dbUtils.updateQuery(TransferConstants.NATURE_RELATIONSHIP_STATUS_TABLE, getColumnValue(model),
        getConditionalColumnValue(model));
    return null;
  }
  
  private Map<String, Object> getColumnValue(IProcessRelationshipDetailsModel model)
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(IProcessRelationshipDetailsModel.STATUS, model.getStatus());
    mapToReturn.put(IProcessRelationshipDetailsModel.FAILURE_MESSAGE, model.getFailureMessage());
    return mapToReturn;
  }
  
  private Map<String, Object> getConditionalColumnValue(IProcessRelationshipDetailsModel model)
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(IProcessRelationshipDetailsModel.PROCESS_INSTANCE_ID,
        model.getProcessInstanceId());
    mapToReturn.put(IProcessRelationshipDetailsModel.COMPONENT_ID, model.getComponentId());
    mapToReturn.put(IProcessRelationshipDetailsModel.ENTITY_ID, model.getEntityId());
    return mapToReturn;
  }
}
