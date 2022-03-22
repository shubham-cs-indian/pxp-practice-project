package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.interactor.model.relationship.GetReferencedRelationshipsAndElementsModel;
import com.cs.core.config.interactor.model.relationship.IGetReferencedRelationshipsAndElementsModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListWithUserModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetRelationshipsAndReferencedElementsByIdsStrategy extends OrientDBBaseStrategy
    implements IGetRelationshipsAndReferencedElementsByIdsStrategy {
  
  public static final String useCase = "GetUnlinkedRelationshipsByKlassIds";
  
  @Override
  public IGetReferencedRelationshipsAndElementsModel execute(IIdsListWithUserModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    requestMap.put("userId", model.getUserId());
    return execute(useCase, requestMap, GetReferencedRelationshipsAndElementsModel.class);
  }
}
