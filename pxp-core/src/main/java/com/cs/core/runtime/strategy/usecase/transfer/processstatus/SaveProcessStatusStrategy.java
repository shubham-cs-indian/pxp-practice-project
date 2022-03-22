package com.cs.core.runtime.strategy.usecase.transfer.processstatus;

import com.cs.core.config.interactor.model.processdetails.IProcessStatusDetailsModel;
import com.cs.core.runtime.interactor.constants.application.TransferConstants;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.strategy.db.BasePostgresStrategy;
import com.cs.core.runtime.strategy.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SaveProcessStatusStrategy extends BasePostgresStrategy
    implements ISaveProcessStatusStrategy {
  
  @Autowired
  DbUtils dbUtils;
  
  @Override
  public IModel execute(IProcessStatusDetailsModel model) throws Exception
  {
    dbUtils.updateQuery(TransferConstants.PROCESS_STATUS_TABLE, getColumnValue(model),
        getConditionalColumnValue(model));
    return null;
  }
  
  private Map<String, Object> getColumnValue(IProcessStatusDetailsModel model)
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(IProcessStatusDetailsModel.STATUS, model.getStatus());
    mapToReturn.put(IProcessStatusDetailsModel.TOTAL_COUNT, model.getTotalCount());
    mapToReturn.put(IProcessStatusDetailsModel.SUCCESS_COUNT, model.getSuccessCount());
    mapToReturn.put(IProcessStatusDetailsModel.FAILED_COUNT, model.getFailedCount());
    mapToReturn.put(IProcessStatusDetailsModel.INPROGRESS_COUNT, model.getInprogressCount());
    mapToReturn.put(IProcessStatusDetailsModel.END_TIME, model.getEndTime());
    return mapToReturn;
  }
  
  private Map<String, Object> getConditionalColumnValue(IProcessStatusDetailsModel model)
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(IProcessStatusDetailsModel.PROCESS_INSTANCE_ID, model.getProcessInstanceId());
    mapToReturn.put(IProcessStatusDetailsModel.COMPONENT_ID, model.getComponentId());
    return mapToReturn;
  }
}
