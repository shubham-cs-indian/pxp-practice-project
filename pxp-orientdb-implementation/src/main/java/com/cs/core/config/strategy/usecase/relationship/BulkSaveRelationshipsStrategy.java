package com.cs.core.config.strategy.usecase.relationship;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.relationship.BulkSaveRelationshipsResponseModel;
import com.cs.core.config.interactor.model.relationship.IBulkSaveRelationshipsModel;
import com.cs.core.config.interactor.model.relationship.IBulkSaveRelationshipsResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class BulkSaveRelationshipsStrategy extends OrientDBBaseStrategy
    implements IBulkSaveRelationshipsStrategy {
  
  public static final String useCase = "BulkSaveRelationships";
  
  @Override
  public IBulkSaveRelationshipsResponseModel execute(IListModel<IBulkSaveRelationshipsModel> model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model);
    return execute(useCase, requestMap, BulkSaveRelationshipsResponseModel.class);
  }
}
