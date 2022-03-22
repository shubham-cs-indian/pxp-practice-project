package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.interactor.model.relationship.IRelationshipInformationModel;
import com.cs.core.config.interactor.model.relationship.RelationshipInformationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetRelationshipInformationStrategy extends OrientDBBaseStrategy
    implements IGetRelationshipInformationStrategy {
  
  @Override
  public IRelationshipInformationModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IIdParameterModel.ID, model.getId());
    return execute(GET_RELATIONSHIP_INFORMATION, requestMap, RelationshipInformationModel.class);
  }
}
