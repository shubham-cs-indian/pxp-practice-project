package com.cs.core.config.strategy.usecase.supplier;

import com.cs.core.config.interactor.model.klass.GetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getSupplierWithoutKPStrategy")
public class GetSupplierWithoutKPStrategy extends OrientDBBaseStrategy
    implements IGetSupplierWithoutKPStrategy {
  
  @Override
  public IGetKlassEntityWithoutKPModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(GET_SUPPLIER_WITHOUT_KP, requestMap, GetKlassEntityWithoutKPModel.class);
  }
}
