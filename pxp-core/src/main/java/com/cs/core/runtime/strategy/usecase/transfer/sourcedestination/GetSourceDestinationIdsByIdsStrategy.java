package com.cs.core.runtime.strategy.usecase.transfer.sourcedestination;

import com.cs.core.config.interactor.model.processdetails.IGetAllInstanceIdByProcessIdsModel;
import com.cs.core.config.interactor.model.processdetails.IProcessSourceDestinationDetailsModel;
import com.cs.core.config.interactor.model.processdetails.ISourceDestinationResponseModel;
import com.cs.core.config.interactor.model.processdetails.SourceDestinationResponseModel;
import com.cs.core.runtime.interactor.constants.application.TransferConstants;
import com.cs.core.runtime.strategy.db.BasePostgresStrategy;
import com.cs.core.runtime.strategy.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GetSourceDestinationIdsByIdsStrategy extends BasePostgresStrategy
    implements IGetSourceDestinationIdsByIdsStrategy {
  
  @Autowired
  DbUtils dbUtils;
  
  @Override
  public ISourceDestinationResponseModel execute(IGetAllInstanceIdByProcessIdsModel model)
      throws Exception
  {
    List<String> columnNamesToFetch = new ArrayList<>();
    columnNamesToFetch.add(IProcessSourceDestinationDetailsModel.SOURCE_ID);
    columnNamesToFetch.add(IProcessSourceDestinationDetailsModel.DESTINATION_ID);
    List<Map<String, Object>> executeQueryWithResult = dbUtils.selectQuery(
        TransferConstants.SOURCE_DESTINATION_TABLE, columnNamesToFetch,
        getCondtionalColumns(model));
    Map<String, String> sourceDestinationIds = new HashMap<>();
    for (Map<String, Object> dataMap : executeQueryWithResult) {
      sourceDestinationIds.put(
          (String) dataMap.get(IProcessSourceDestinationDetailsModel.SOURCE_ID),
          (String) dataMap.get(IProcessSourceDestinationDetailsModel.DESTINATION_ID));
    }
    
    ISourceDestinationResponseModel returnModel = new SourceDestinationResponseModel();
    returnModel.setSourceDestination(sourceDestinationIds);
    return returnModel;
  }
  
  private Map<String, Object> getCondtionalColumns(IGetAllInstanceIdByProcessIdsModel model)
  {
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IProcessSourceDestinationDetailsModel.PROCESS_INSTANCE_ID,
        model.getProcessInstanceId());
    returnMap.put(IProcessSourceDestinationDetailsModel.SOURCE_ID, model.getInstanceIds()
        .getIds());
    return returnMap;
  }
}
