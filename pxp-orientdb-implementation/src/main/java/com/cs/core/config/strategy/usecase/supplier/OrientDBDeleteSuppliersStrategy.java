package com.cs.core.config.strategy.usecase.supplier;

import com.cs.core.config.interactor.model.klass.BulkDeleteKlassResponseModel;
import com.cs.core.config.interactor.model.klass.IBulkDeleteKlassResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBDeleteSuppliersStrategy extends OrientDBBaseStrategy
    implements IDeleteSuppliersStrategy {
  
  public static final String useCase = "DeleteSuppliers";
  
  @Override
  public IBulkDeleteKlassResponseModel execute(IIdsListParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    return execute(useCase, requestMap, BulkDeleteKlassResponseModel.class);
  }
}
