package com.cs.core.config.strategy.usecase.condition;

import com.cs.core.config.interactor.model.condition.ConditionModel;
import com.cs.core.config.interactor.model.condition.IConditionModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetConditionStrategy extends OrientDBBaseStrategy
    implements IGetConditionStrategy {
  
  public static final String useCase = "GetCondition";
  
  @Override
  public IConditionModel execute(IIdParameterModel model) throws Exception
  {
    
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("condition", model);
    return super.execute(useCase, requestMap, ConditionModel.class);
  }
}
