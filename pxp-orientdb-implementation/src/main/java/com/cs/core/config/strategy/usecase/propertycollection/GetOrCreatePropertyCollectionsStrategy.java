package com.cs.core.config.strategy.usecase.propertycollection;

import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.propertycollection.PropertyCollection;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.usecase.propertycollection.IGetOrCreatePropertyCollectionStrategy;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getOrCreatePropertyCollectionsStrategy")
public class GetOrCreatePropertyCollectionsStrategy extends OrientDBBaseStrategy
    implements IGetOrCreatePropertyCollectionStrategy {
  
  @SuppressWarnings("unchecked")
  @Override
  public IListModel<IPropertyCollection> execute(IListModel<IPropertyCollection> model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("propertyCollections", model);
    return execute(GET_OR_CREATE_PROPERTY_COLLECTIONS, requestMap,
        new TypeReference<ListModel<PropertyCollection>>()
        {
          
        });
  }
}
