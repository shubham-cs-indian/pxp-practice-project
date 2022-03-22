package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.klass.GetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBCreateKlassStrategy extends OrientDBBaseStrategy
    implements ICreateKlassStrategy {
  
  @Override
  public IGetKlassEntityWithoutKPModel execute(IKlassModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("klass", model);
    return super.execute(OrientDBBaseStrategy.CREATE_KLASS, requestMap,
        GetKlassEntityWithoutKPModel.class);
  }
}
