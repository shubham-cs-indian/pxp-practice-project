package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.interactor.model.relationship.GetRelationshipModel;
import com.cs.core.config.interactor.model.relationship.ICreateRelationshipModel;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBCreateRelationshipStrategy extends OrientDBBaseStrategy
    implements ICreateRelationshipStrategy {
  
  @Override
  public IGetRelationshipModel execute(ICreateRelationshipModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("relationship", model);
    return execute(CREATE_RELATIONSHIP, requestMap, GetRelationshipModel.class);
  }
}
