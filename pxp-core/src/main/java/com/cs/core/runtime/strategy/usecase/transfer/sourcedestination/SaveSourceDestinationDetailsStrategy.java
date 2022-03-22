package com.cs.core.runtime.strategy.usecase.transfer.sourcedestination;

import com.cs.core.config.interactor.model.processdetails.IProcessSourceDestinationDetailsModel;
import com.cs.core.runtime.interactor.constants.application.TransferConstants;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.strategy.db.BasePostgresStrategy;
import com.cs.core.runtime.strategy.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SaveSourceDestinationDetailsStrategy extends BasePostgresStrategy
    implements ISaveSourceDestinationDetailsStrategy {
  
  @Autowired
  DbUtils dbUtils;
  
  @Override
  public IModel execute(IProcessSourceDestinationDetailsModel model) throws Exception
  {
    dbUtils.updateQuery(TransferConstants.SOURCE_DESTINATION_TABLE, getColumnValue(model),
        getConditionalColumnValue(model));
    return null;
  }
  
  private Map<String, Object> getColumnValue(IProcessSourceDestinationDetailsModel model)
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(IProcessSourceDestinationDetailsModel.SOURCE_ID, model.getSourceId());
    mapToReturn.put(IProcessSourceDestinationDetailsModel.DESTINATION_ID, model.getDestinationId());
    return mapToReturn;
  }
  
  private Map<String, Object> getConditionalColumnValue(IProcessSourceDestinationDetailsModel model)
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(IProcessSourceDestinationDetailsModel.PROCESS_INSTANCE_ID,
        model.getProcessInstanceId());
    mapToReturn.put(IProcessSourceDestinationDetailsModel.COMPONENT_ID, model.getComponentId());
    mapToReturn.put(IProcessSourceDestinationDetailsModel.SOURCE_ID, model.getSourceId());
    return mapToReturn;
  }
}
