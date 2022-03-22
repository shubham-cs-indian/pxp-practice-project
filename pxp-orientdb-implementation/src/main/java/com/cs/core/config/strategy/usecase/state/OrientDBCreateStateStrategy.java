package com.cs.core.config.strategy.usecase.state;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.state.GetStateResponseModel;
import com.cs.core.config.interactor.model.state.IGetStateResponseModel;
import com.cs.core.config.interactor.model.state.IStateModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class OrientDBCreateStateStrategy extends OrientDBBaseStrategy
    implements ICreateStateStrategy {
  
  public static final String useCase = "CreateState";
  
  @Override
  public IGetStateResponseModel execute(IStateModel model) throws Exception
  {
    
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("state", model);
    return execute(useCase, requestMap, GetStateResponseModel.class);
  }
}
