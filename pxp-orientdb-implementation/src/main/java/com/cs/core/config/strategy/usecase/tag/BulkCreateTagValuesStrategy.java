package com.cs.core.config.strategy.usecase.tag;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.BulkCreateTagValuesResponseModel;
import com.cs.core.config.interactor.model.tag.IBulkCreateTagValuesResponseModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BulkCreateTagValuesStrategy extends OrientDBBaseStrategy
    implements IBulkCreateTagValuesStrategy {
  
  @Override
  public IBulkCreateTagValuesResponseModel execute(IListModel<ITagModel> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model);
    return execute(BULK_CREATE_TAG_VALUES, requestMap, BulkCreateTagValuesResponseModel.class);
  }
}
