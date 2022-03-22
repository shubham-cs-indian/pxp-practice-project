package com.cs.core.config.strategy.usecase.staticcollection;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.collections.GetStaticCollectionInfoModel;
import com.cs.core.runtime.interactor.model.collections.IGetStaticCollectionInfoModel;
import com.cs.core.runtime.interactor.model.collections.ISaveStaticCollectionDetailsModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SaveStaticCollectionDetailsStrategy extends OrientDBBaseStrategy
    implements ISaveStaticCollectionDetailsStrategy {
  
  @Override
  public IGetStaticCollectionInfoModel execute(ISaveStaticCollectionDetailsModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("collection", model);
    return execute(SAVE_COLLECTION_DETAILS, requestMap, GetStaticCollectionInfoModel.class);
  }
}
