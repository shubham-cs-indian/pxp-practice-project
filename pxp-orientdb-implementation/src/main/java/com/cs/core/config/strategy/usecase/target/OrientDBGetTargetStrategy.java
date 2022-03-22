package com.cs.core.config.strategy.usecase.target;

import com.cs.core.config.interactor.model.klass.AbstractKlassModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getTargetStrategy")
public class OrientDBGetTargetStrategy extends OrientDBBaseStrategy implements IGetTargetStrategy {
  
  public static final String useCase = "GetTarget";
  
  @Override
  public ITargetModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return (ITargetModel) execute(useCase, requestMap, AbstractKlassModel.class);
  }
}
