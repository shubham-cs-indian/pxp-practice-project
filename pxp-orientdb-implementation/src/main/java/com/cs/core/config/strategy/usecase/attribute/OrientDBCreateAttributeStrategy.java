package com.cs.core.config.strategy.usecase.attribute;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.attribute.CreateAttributeResponseModel;
import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.attribute.ICreateAttributeResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component("createAttributeStrategy")
public class OrientDBCreateAttributeStrategy extends OrientDBBaseStrategy
    implements ICreateAttributeStrategy {
  
  @Override
  public ICreateAttributeResponseModel execute(IAttributeModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("attribute", (IAttribute) model.getEntity());
    return super.execute(OrientDBBaseStrategy.CREATE_ATTRIBUTE, requestMap,
        CreateAttributeResponseModel.class);
  }
}
