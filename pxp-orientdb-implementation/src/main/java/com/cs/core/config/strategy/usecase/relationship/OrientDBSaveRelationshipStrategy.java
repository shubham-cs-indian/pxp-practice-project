package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.interactor.model.relationship.ISaveRelationshipModel;
import com.cs.core.config.interactor.model.relationship.ISaveRelationshipStrategyResponseModel;
import com.cs.core.config.interactor.model.relationship.SaveRelationshipStrategyResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBSaveRelationshipStrategy extends OrientDBBaseStrategy
    implements ISaveRelationshipStrategy {
  
  @Override
  public ISaveRelationshipStrategyResponseModel execute(ISaveRelationshipModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("relationship", model);
    return execute(SAVE_RELATIONSHIP, requestMap, SaveRelationshipStrategyResponseModel.class);
  }
}
