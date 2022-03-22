package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.klass.GetKlassEntityWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBSaveKlassStrategy extends OrientDBBaseStrategy implements ISaveKlassStrategy {
  
  @Override
  public IGetKlassEntityWithoutKPStrategyResponseModel execute(IKlassSaveModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("klass", model);
    return super.execute(OrientDBBaseStrategy.SAVE_KLASS, requestMap,
        GetKlassEntityWithoutKPStrategyResponseModel.class);
  }
}
