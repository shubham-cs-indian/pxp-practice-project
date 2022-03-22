package com.cs.core.config.strategy.usecase.state;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.state.GetStateResponseModel;
import com.cs.core.config.interactor.model.state.IGetStateResponseModel;
import com.cs.core.config.interactor.model.state.IStateModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class OrientDBSaveStateStrategy extends OrientDBBaseStrategy implements ISaveStateStrategy {
  
  public static final String useCase = "SaveState";
  
  @Override
  public IGetStateResponseModel execute(IStateModel model) throws Exception
  {
    
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("state", model);
    return execute(useCase, requestMap, GetStateResponseModel.class);
  }
}
