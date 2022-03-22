package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.interactor.model.relationship.GetRelationshipModel;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetRelationshipStrategy extends OrientDBBaseStrategy
    implements IGetRelationshipStrategy {
  
  @Override
  public IGetRelationshipModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(GET_RELATIONSHIP, requestMap, GetRelationshipModel.class);
  }
}
