package com.cs.core.config.strategy.usecase.datarule;

import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBCreateDataRuleStrategy extends OrientDBBaseStrategy
    implements ICreateDataRuleStrategy {
  
  @Override
  public IDataRuleModel execute(IDataRuleModel model) throws Exception
  {
    
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("rule", model);
    return execute(OrientDBBaseStrategy.CREATE_DATA_RULE, requestMap, DataRuleModel.class);
  }
}
