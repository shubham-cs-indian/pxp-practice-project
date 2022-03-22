package com.cs.core.config.strategy.usecase.staticcollection;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SaveStaticCollectionNodeStrategy extends OrientDBBaseStrategy
    implements ISaveStaticCollectionNodeStrategy {
  
  @Override
  public IIdLabelModel execute(IIdLabelModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("collection", model);
    execute(SAVE_COLLECTION_NODE, requestMap);
    return null;
  }
}
