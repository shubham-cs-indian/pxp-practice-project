package com.cs.core.config.strategy.usecase.standard.attribute;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.attribute.CreateAttributeResponseModel;
import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.attribute.ICreateAttributeResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.attribute.ICreateAttributeStrategy;

@Component("createStandardAttributesStrategy")
public class OrientDBCreateStandardAttributesStrategy extends OrientDBBaseStrategy
    implements ICreateAttributeStrategy {
  
  public static final String useCase = "CreateStandardAttribute";
  
  @Override
  public ICreateAttributeResponseModel execute(IAttributeModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("attribute", (IAttribute) model.getEntity());
    return execute(useCase, requestMap, CreateAttributeResponseModel.class);
  }
}
