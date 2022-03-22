package com.cs.core.config.strategy.usecase.target;

import com.cs.core.config.interactor.model.klass.BulkDeleteKlassResponseModel;
import com.cs.core.config.interactor.model.klass.IBulkDeleteKlassResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBDeleteTargetsStrategy extends OrientDBBaseStrategy
    implements IDeleteTargetsStrategy {
  
  public static final String useCase = "DeleteTargets";
  
  @Override
  public IBulkDeleteKlassResponseModel execute(IIdsListParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    return execute(useCase, requestMap, BulkDeleteKlassResponseModel.class);
  }
}
