package com.cs.core.runtime.strategy.usecase.transfer.processstatus;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.processdetails.IProcessStatusDetailsModel;
import com.cs.core.config.interactor.model.processdetails.ProcessStatusDetailsModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.TransferConstants;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.strategy.db.BasePostgresStrategy;
import com.cs.core.runtime.strategy.utils.DbUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class GetProcessStatusByProcessInstanceIdStrategy extends BasePostgresStrategy
    implements IGetProcessStatusByProcessInstanceIdStrategy {
  
  @Autowired
  DbUtils dbUtils;
  
  @Override
  public IListModel<IProcessStatusDetailsModel> execute(IProcessStatusDetailsModel dataModel)
      throws Exception
  {
    Map<String, Object> executeQueryWithResult = dbUtils.selectProcessStatusQuery(
        TransferConstants.PROCESS_STATUS_TABLE, new ArrayList<>(), getConditionalMap(dataModel));
    if (executeQueryWithResult.isEmpty()) {
      throw new NotFoundException();
    }
    String jsonStringOftheValue = ObjectMapperUtil.writeValueAsString(executeQueryWithResult);
    return ObjectMapperUtil.readValue(jsonStringOftheValue,
        new TypeReference<ListModel<ProcessStatusDetailsModel>>()
        {
          
        });
  }
  
  private Map<String, Object> getConditionalMap(IProcessStatusDetailsModel dataModel)
  {
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IProcessStatusDetailsModel.PROCESS_INSTANCE_ID, dataModel.getProcessInstanceId());
    return returnMap;
  }
}
