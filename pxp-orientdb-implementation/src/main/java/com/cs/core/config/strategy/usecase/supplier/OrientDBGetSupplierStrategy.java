package com.cs.core.config.strategy.usecase.supplier;

import com.cs.core.config.interactor.model.supplier.ISupplierModel;
import com.cs.core.config.interactor.model.supplier.SupplierModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getSupplierStrategy")
public class OrientDBGetSupplierStrategy extends OrientDBBaseStrategy
    implements IGetSupplierStrategy {
  
  public static final String useCase = "GetSupplier";
  
  @Override
  public ISupplierModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(useCase, requestMap, SupplierModel.class);
  }
}
