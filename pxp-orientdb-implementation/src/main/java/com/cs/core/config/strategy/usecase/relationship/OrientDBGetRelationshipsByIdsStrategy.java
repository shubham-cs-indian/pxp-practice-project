package com.cs.core.config.strategy.usecase.relationship;

import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.Relationship;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetRelationshipsByIdsStrategy extends OrientDBBaseStrategy
    implements IGetRelationshipsStrategy {
  
  public static final String useCase = "GetRelationshipsById";
  
  @Override
  public IListModel<IRelationship> execute(IIdsListParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    return execute(useCase, requestMap, new TypeReference<ListModel<Relationship>>()
    {
      
    });
  }
}
