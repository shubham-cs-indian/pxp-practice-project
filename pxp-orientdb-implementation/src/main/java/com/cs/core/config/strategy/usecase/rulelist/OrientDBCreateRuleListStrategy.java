package com.cs.core.config.strategy.usecase.rulelist;

import com.cs.core.config.interactor.model.rulelist.IRuleListModel;
import com.cs.core.config.interactor.model.rulelist.RuleListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBCreateRuleListStrategy extends OrientDBBaseStrategy
    implements ICreateRuleListStrategy {
  
  @Override
  public IRuleListModel execute(IRuleListModel model) throws Exception
  {
    
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ruleList", model);
    return execute(CREATE_RULE_LIST, requestMap, RuleListModel.class);
  }
}
