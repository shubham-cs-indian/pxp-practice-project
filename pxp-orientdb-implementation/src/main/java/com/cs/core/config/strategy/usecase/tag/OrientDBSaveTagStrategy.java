package com.cs.core.config.strategy.usecase.tag;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.BulkSaveTagResponseModel;
import com.cs.core.config.interactor.model.tag.IBulkSaveTagResponseModel;
import com.cs.core.config.interactor.model.tag.ISaveTagModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBSaveTagStrategy extends OrientDBBaseStrategy implements ISaveTagStrategy {
  
  @Override
  public IBulkSaveTagResponseModel execute(IListModel<ISaveTagModel> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model);
    return super.execute(OrientDBBaseStrategy.SAVE_TAG, requestMap, BulkSaveTagResponseModel.class);
  }
}
