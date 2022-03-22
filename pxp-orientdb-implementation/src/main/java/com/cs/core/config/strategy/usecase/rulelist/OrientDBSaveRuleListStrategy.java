package com.cs.core.config.strategy.usecase.rulelist;

import com.cs.core.config.interactor.model.rulelist.IRuleListModel;
import com.cs.core.config.interactor.model.rulelist.IRuleListStrategySaveModel;
import com.cs.core.config.interactor.model.rulelist.RuleListStrategySaveModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBSaveRuleListStrategy extends OrientDBBaseStrategy
    implements ISaveRuleListStrategy {
  
  @Override
  public IRuleListStrategySaveModel execute(IRuleListModel model) throws Exception
  {
    
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ruleList", model);
    return execute(SAVE_RULE_LIST, requestMap, RuleListStrategySaveModel.class);
  }
}
