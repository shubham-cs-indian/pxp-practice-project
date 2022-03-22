package com.cs.core.config.strategy.usecase.supplier;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetOrCreateSupplierStrategy extends OrientDBBaseStrategy
    implements IGetOrCreateSupplierStrategy {
  
  public static final String useCase = "GetOrCreateSupplier";
  
  @Override
  public ISupplierModel execute(IListModel<ISupplierModel> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model);
    execute(useCase, requestMap);
    /*ISupplier savedSupplierKlass = ObjectMapperUtil.readValue(response, ISupplier.class);
    return new SupplierModel((Supplier) savedSupplierKlass);*/
    return null;
  }
}
