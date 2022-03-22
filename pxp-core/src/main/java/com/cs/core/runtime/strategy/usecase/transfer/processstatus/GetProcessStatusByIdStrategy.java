package com.cs.core.runtime.strategy.usecase.transfer.processstatus;

import com.cs.core.config.interactor.model.processdetails.IProcessStatusDetailsModel;
import com.cs.core.config.interactor.model.processdetails.ProcessStatusDetailsModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.TransferConstants;
import com.cs.core.runtime.strategy.db.BasePostgresStrategy;
import com.cs.core.runtime.strategy.utils.DbUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GetProcessStatusByIdStrategy extends BasePostgresStrategy
    implements IGetProcessStatusByIdStrategy {
  
  @Autowired
  DbUtils dbUtils;
  
  @Override
  public IProcessStatusDetailsModel execute(IProcessStatusDetailsModel dataModel) throws Exception
  {
    List<Map<String, Object>> executeQueryWithResult = dbUtils.selectQuery(
        TransferConstants.PROCESS_STATUS_TABLE, new ArrayList<>(), getConditionalMap(dataModel));
    if (executeQueryWithResult.isEmpty()) {
      throw new NotFoundException();
    }
    String jsonStringOftheValue = ObjectMapperUtil
        .writeValueAsString(executeQueryWithResult.get(0));
    return ObjectMapperUtil.readValue(jsonStringOftheValue, ProcessStatusDetailsModel.class);
  }
  
  private Map<String, Object> getConditionalMap(IProcessStatusDetailsModel dataModel)
  {
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IProcessStatusDetailsModel.PROCESS_INSTANCE_ID, dataModel.getProcessInstanceId());
    returnMap.put(IProcessStatusDetailsModel.COMPONENT_ID, dataModel.getComponentId());
    return returnMap;
  }
}
