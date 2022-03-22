package com.cs.core.config.strategy.usecase.propertycollection;

import com.cs.core.config.interactor.model.propertycollection.GetPropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.IGetPropertyCollectionModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getPropertyCollectionStrategy")
public class GetPropertyCollectionStrategy extends OrientDBBaseStrategy
    implements IGetPropertyCollectionStrategy {
  
  @Override
  public IGetPropertyCollectionModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(GET_PROPERTY_COLLECTION, requestMap, GetPropertyCollectionModel.class);
  }
}
