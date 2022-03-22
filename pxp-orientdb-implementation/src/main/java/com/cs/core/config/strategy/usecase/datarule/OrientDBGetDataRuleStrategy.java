package com.cs.core.config.strategy.usecase.datarule;

import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetDataRuleStrategy extends OrientDBBaseStrategy
    implements IGetDataRuleStrategy {
  
  @Override
  public IDataRuleModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(OrientDBBaseStrategy.GET_DATA_RULE, requestMap, DataRuleModel.class);
  }
}
