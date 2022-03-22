package com.cs.core.config.strategy.usecase.target;

import com.cs.core.config.interactor.model.klass.GetKlassEntityWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBSaveTargetStrategy extends OrientDBBaseStrategy
    implements ISaveTargetStrategy {
  
  public static final String useCase = "SaveTarget";
  
  @Override
  public IGetKlassEntityWithoutKPStrategyResponseModel execute(IKlassSaveModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("target", model);
    return execute(useCase, requestMap, GetKlassEntityWithoutKPStrategyResponseModel.class);
  }
}
