package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.IGetDefaultKlassesModel;
import com.cs.core.config.interactor.model.klass.GetDefaultKlassesModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.typeswitch.IGetAllowedTypesModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetDefaultKlassesStrategy extends OrientDBBaseStrategy
    implements IGetDefaultKlassesStrategy {
  
  public static final String useCase = "GetDefaultKlasses";
  
  @Override
  public IGetDefaultKlassesModel execute(IGetAllowedTypesModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    requestMap.put("mode", model.getMode());
    requestMap.put("standardKlassId", model.getStandardKlassId());
    return execute(useCase, requestMap, GetDefaultKlassesModel.class);
  }
}
