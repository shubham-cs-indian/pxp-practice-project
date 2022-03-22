package com.cs.core.config.strategy.usecase.attribute;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.AttributeInfoModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IAttributeInfoModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetAttributeListStrategy extends OrientDBBaseStrategy
    implements IGetAllAttributeListStrategy {
  
  @Override
  public IListModel<IAttributeInfoModel> execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("mode", model.getId());
    return super.execute(OrientDBBaseStrategy.GET_ATTRIBUTE_LIST, requestMap,
        new TypeReference<ListModel<AttributeInfoModel>>()
        {
          
        });
  }
}
