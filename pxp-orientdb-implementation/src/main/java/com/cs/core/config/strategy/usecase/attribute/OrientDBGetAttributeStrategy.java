package com.cs.core.config.strategy.usecase.attribute;

import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.configdetails.AbstractAttributeModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetAttributeStrategy extends OrientDBBaseStrategy
    implements IGetAttributeStrategy {
  
  @Override
  public IAttributeModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return super.execute(OrientDBBaseStrategy.GET_ATTRIBUTE, requestMap,
        AbstractAttributeModel.class);
  }
}
