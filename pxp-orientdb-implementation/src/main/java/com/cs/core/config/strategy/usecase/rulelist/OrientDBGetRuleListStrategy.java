package com.cs.core.config.strategy.usecase.rulelist;

import com.cs.core.config.interactor.model.rulelist.IRuleListModel;
import com.cs.core.config.interactor.model.rulelist.RuleListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetRuleListStrategy extends OrientDBBaseStrategy
    implements IGetRuleListStrategy {
  
  @Override
  public IRuleListModel execute(IIdParameterModel model) throws Exception
  {
    
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ruleList", model);
    return execute(GET_RULE_LIST, requestMap, RuleListModel.class);
  }
}
