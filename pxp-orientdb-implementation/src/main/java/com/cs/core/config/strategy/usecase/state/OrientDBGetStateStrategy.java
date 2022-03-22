package com.cs.core.config.strategy.usecase.state;

import com.cs.core.config.interactor.model.state.IStateModel;
import com.cs.core.config.interactor.model.state.StateModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetStateStrategy extends OrientDBBaseStrategy implements IGetStateStrategy {
  
  public static final String useCase = "GetState";
  
  @Override
  public IStateModel execute(IIdParameterModel model) throws Exception
  {
    
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("state", model);
    return execute(useCase, requestMap, StateModel.class);
  }
}
