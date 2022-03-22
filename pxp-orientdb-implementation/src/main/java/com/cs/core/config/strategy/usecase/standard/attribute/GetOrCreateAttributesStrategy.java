package com.cs.core.config.strategy.usecase.standard.attribute;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.attribute.IGetOrCreateAttributeStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getOrCreateStandardAttributesStrategy")
public class GetOrCreateAttributesStrategy extends OrientDBBaseStrategy
    implements IGetOrCreateAttributeStrategy {
  
  public static final String useCase = "GetOrCreateAttribute";
  
  @Override
  public IListModel<IAttribute> execute(IListModel<IAttribute> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("attribute", model);
    return execute(useCase, requestMap, new TypeReference<ListModel<IAttribute>>()
    {
      
    });
  }
}
