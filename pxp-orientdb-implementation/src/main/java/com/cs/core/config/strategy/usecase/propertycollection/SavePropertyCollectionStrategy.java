package com.cs.core.config.strategy.usecase.propertycollection;

import com.cs.core.config.interactor.model.propertycollection.ISavePropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.ISavePropertyCollectionResponseModel;
import com.cs.core.config.interactor.model.propertycollection.SavePropertyCollectionResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("savePropertyCollectionStrategy")
public class SavePropertyCollectionStrategy extends OrientDBBaseStrategy
    implements ISavePropertyCollectionStrategy {
  
  @Override
  public ISavePropertyCollectionResponseModel execute(ISavePropertyCollectionModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("propertyCollection", model);
    return execute(SAVE_PROPERTY_COLLECTION, requestMap, SavePropertyCollectionResponseModel.class);
  }
}
