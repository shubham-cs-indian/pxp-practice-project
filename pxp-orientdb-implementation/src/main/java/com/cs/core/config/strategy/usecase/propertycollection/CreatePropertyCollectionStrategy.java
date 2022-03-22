package com.cs.core.config.strategy.usecase.propertycollection;

import com.cs.core.config.interactor.model.propertycollection.GetPropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.IGetPropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.IPropertyCollectionModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("createPropertyCollectionStrategy")
public class CreatePropertyCollectionStrategy extends OrientDBBaseStrategy
    implements ICreatePropertyCollectionStrategy {
  
  @Override
  public IGetPropertyCollectionModel execute(IPropertyCollectionModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("propertyCollection", model.getEntity());
    return execute(CREATE_PROPERTY_COLLECTION, requestMap, GetPropertyCollectionModel.class);
  }
}
