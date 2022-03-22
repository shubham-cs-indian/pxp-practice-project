package com.cs.core.config.strategy.usecase.datarule;

import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.datarule.ISaveDataRuleModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBSaveDataRuleStrategy extends OrientDBBaseStrategy
    implements ISaveDataRuleStrategy {
  
  @Override
  public IDataRuleModel execute(ISaveDataRuleModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("rule", model);
    return execute(OrientDBBaseStrategy.SAVE_DATA_RULE, requestMap, DataRuleModel.class);
  }
}
