package com.cs.core.config.strategy.usecase.relationship;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.relationship.GetRelationshipModel;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Component
public class OrientDBGetRootRelationshipSideInfoStrategy extends OrientDBBaseStrategy
    implements IGetRootRelationshipSideInfo {
  
  @Override
  public IGetRelationshipModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(GET_ROOT_RELATIONSHIP_SIDE_INFO, requestMap, GetRelationshipModel.class);
  }
}
