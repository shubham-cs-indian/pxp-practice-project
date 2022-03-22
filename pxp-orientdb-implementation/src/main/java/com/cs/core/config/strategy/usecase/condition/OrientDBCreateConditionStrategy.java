package com.cs.core.config.strategy.usecase.condition;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.condition.ConditionResponseModel;
import com.cs.core.config.interactor.model.condition.IConditionModel;
import com.cs.core.config.interactor.model.condition.IConditionResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class OrientDBCreateConditionStrategy extends OrientDBBaseStrategy
    implements ICreateConditionStrategy {
  
  public static final String useCase = "CreateCondition";
  
  @Override
  public IConditionResponseModel execute(IConditionModel model) throws Exception
  {
    
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("condition", model);
    return super.execute(useCase, requestMap, ConditionResponseModel.class);
  }
}
