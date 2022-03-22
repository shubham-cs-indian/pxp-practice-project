package com.cs.core.config.strategy.usecase.target;

import com.cs.core.config.interactor.model.klass.GetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBCreateTargetStrategy extends OrientDBBaseStrategy
    implements ICreateTargetStrategy {
  
  public static final String useCase = "CreateTarget";
  
  @Override
  public IGetKlassEntityWithoutKPModel execute(ITargetModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("target", model.getEntity());
    return execute(useCase, requestMap, GetKlassEntityWithoutKPModel.class);
  }
}
