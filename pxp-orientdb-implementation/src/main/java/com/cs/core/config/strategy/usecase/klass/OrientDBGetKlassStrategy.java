package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.klass.GetKlassModel;
import com.cs.core.config.interactor.model.klass.IGetKlassModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetKlassStrategy extends OrientDBBaseStrategy implements IGetKlassStrategy {
  
  @Override
  public IGetKlassModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return super.execute(OrientDBBaseStrategy.GET_KLASS, requestMap, GetKlassModel.class);
  }
}
