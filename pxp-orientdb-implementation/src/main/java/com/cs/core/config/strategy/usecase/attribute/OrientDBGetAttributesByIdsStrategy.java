package com.cs.core.config.strategy.usecase.attribute;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getAttributesByIdsStrategy")
public class OrientDBGetAttributesByIdsStrategy extends OrientDBBaseStrategy
    implements IGetAttributesStrategy {
  
  @Override
  public IListModel<IAttribute> execute(IIdsListParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    return super.execute(OrientDBBaseStrategy.GET_ATTRIBUTES_BY_ID, requestMap,
        new TypeReference<ListModel<IAttribute>>()
        {
          
        });
  }
}
