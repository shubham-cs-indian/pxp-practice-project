package com.cs.core.config.strategy.usecase.propertycollection;

import com.cs.core.config.interactor.model.propertycollection.BulkDeletePropertyCollectionReturnModel;
import com.cs.core.config.interactor.model.propertycollection.IBulkDeletePropertyCollectionReturnModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("deletePropertyCollectionStrategy")
public class DeletePropertyCollectionStrategy extends OrientDBBaseStrategy
    implements IDeletePropertyCollectionStrategy {
  
  @Override
  public IBulkDeletePropertyCollectionReturnModel execute(IIdsListParameterModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    return execute(DELETE_PROPERTY_COLLECTION, requestMap,
        BulkDeletePropertyCollectionReturnModel.class);
  }
}
