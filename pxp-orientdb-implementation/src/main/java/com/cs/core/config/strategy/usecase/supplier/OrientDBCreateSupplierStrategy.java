package com.cs.core.config.strategy.usecase.supplier;

import com.cs.core.config.interactor.model.klass.GetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBCreateSupplierStrategy extends OrientDBBaseStrategy
    implements ICreateSupplierStrategy {
  
  public static final String useCase = "CreateSupplier";
  
  @Override
  public IGetKlassEntityWithoutKPModel execute(ISupplierModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("supplier", model.getEntity());
    return execute(useCase, requestMap, GetKlassEntityWithoutKPModel.class);
  }
}
