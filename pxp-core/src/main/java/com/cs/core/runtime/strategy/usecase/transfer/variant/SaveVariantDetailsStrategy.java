package com.cs.core.runtime.strategy.usecase.transfer.variant;

import com.cs.core.config.interactor.model.processdetails.IProcessVariantStatusModel;
import com.cs.core.runtime.interactor.constants.application.TransferConstants;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.strategy.db.BasePostgresStrategy;
import com.cs.core.runtime.strategy.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SaveVariantDetailsStrategy extends BasePostgresStrategy
    implements ISaveVariantDetailsStrategy {
  
  @Autowired
  DbUtils dbUtils;
  
  @Override
  public IModel execute(IProcessVariantStatusModel model) throws Exception
  {
    dbUtils.updateQuery(TransferConstants.VARIANT_STATUS_TABLE, getColumnValue(model),
        getConditionalColumnValue(model));
    return null;
  }
  
  private Map<String, Object> getColumnValue(IProcessVariantStatusModel model)
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(IProcessVariantStatusModel.ENTITY_TYPE, model.getEntityType());
    mapToReturn.put(IProcessVariantStatusModel.STATUS, model.getStatus());
    mapToReturn.put(IProcessVariantStatusModel.FAILURE_MESSAGE, model.getFailureMessage());
    mapToReturn.put(IProcessVariantStatusModel.KLASS_INSTANCE_ID, model.getKlassInstanceId());
    mapToReturn.put(IProcessVariantStatusModel.PARENT_ID, model.getParentId());
    return mapToReturn;
  }
  
  private Map<String, Object> getConditionalColumnValue(IProcessVariantStatusModel model)
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(IProcessVariantStatusModel.PROCESS_INSTANCE_ID, model.getProcessInstanceId());
    mapToReturn.put(IProcessVariantStatusModel.COMPONENT_ID, model.getComponentId());
    mapToReturn.put(IProcessVariantStatusModel.ENTITY_ID, model.getEntityId());
    return mapToReturn;
  }
}
