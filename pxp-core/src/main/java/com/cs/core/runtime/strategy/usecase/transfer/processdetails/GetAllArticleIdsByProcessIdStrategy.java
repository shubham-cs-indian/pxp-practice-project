package com.cs.core.runtime.strategy.usecase.transfer.processdetails;

import com.cs.core.config.interactor.model.processdetails.IGetAllInstanceIdByProcessIdsModel;
import com.cs.core.config.interactor.model.processdetails.IProcessKlassInstanceStatusModel;
import com.cs.core.runtime.interactor.constants.application.TransferConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.strategy.db.BasePostgresStrategy;
import com.cs.core.runtime.strategy.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GetAllArticleIdsByProcessIdStrategy extends BasePostgresStrategy
    implements IGetAllArticleIdsByProcessIdStrategy {
  
  @Autowired
  DbUtils dbUtils;
  
  @Override
  public IIdsListParameterModel execute(IGetAllInstanceIdByProcessIdsModel model) throws Exception
  {
    List<String> columnNamesToFetch = new ArrayList<>();
    columnNamesToFetch.add(IProcessKlassInstanceStatusModel.ENTITY_ID);
    List<Map<String, Object>> executeQueryWithResult = dbUtils.selectQuery(
        TransferConstants.KLASS_INSTANCE_STATUS_TABLE, columnNamesToFetch,
        getCondtionalColumns(model));
    Set<String> articleIds = new HashSet<>();
    for (Map<String, Object> dataMap : executeQueryWithResult) {
      String entityId = (String) dataMap.get(IProcessKlassInstanceStatusModel.ENTITY_ID);
      articleIds.add(entityId);
    }
    IIdsListParameterModel returnModel = new IdsListParameterModel();
    returnModel.setIds(new ArrayList<>(articleIds));
    return returnModel;
  }
  
  private Map<String, Object> getCondtionalColumns(IGetAllInstanceIdByProcessIdsModel model)
  {
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IProcessKlassInstanceStatusModel.PROCESS_INSTANCE_ID,
        model.getProcessInstanceId());
    returnMap.put(IProcessKlassInstanceStatusModel.COMPONENT_ID, model.getComponentId());
    returnMap.put(IProcessKlassInstanceStatusModel.STATUS, true);
    return returnMap;
  }
}
