package com.cs.core.config.strategy.usecase.rulelist;

import com.cs.core.config.interactor.model.rulelist.BulkDeleteRuleListReturnModel;
import com.cs.core.config.interactor.model.rulelist.IBulkDeleteRuleListReturnModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBDeleteRuleListStrategy extends OrientDBBaseStrategy
    implements IDeleteRuleListStrategy {
  
  @Override
  public IBulkDeleteRuleListReturnModel execute(IIdsListParameterModel model) throws Exception
  {
    
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    return execute(DELETE_RULE_LIST, requestMap, BulkDeleteRuleListReturnModel.class);
  }
}
