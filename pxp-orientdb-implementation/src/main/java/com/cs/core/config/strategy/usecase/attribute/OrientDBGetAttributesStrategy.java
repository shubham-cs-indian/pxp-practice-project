package com.cs.core.config.strategy.usecase.attribute;

import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.configdetails.AbstractAttributeModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetAttributesStrategy extends OrientDBBaseStrategy
    implements IGetAllAttributesStrategy {
  
  @Override
  public IListModel<IAttributeModel> execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("mode", model.getId());
    return super.execute(OrientDBBaseStrategy.GET_ATTRIBUTES, requestMap,
        new TypeReference<ListModel<AbstractAttributeModel>>()
        {
          
        });
  }
}
