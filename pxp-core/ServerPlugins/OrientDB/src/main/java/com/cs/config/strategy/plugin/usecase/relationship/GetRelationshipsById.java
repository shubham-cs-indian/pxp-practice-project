package com.cs.config.strategy.plugin.usecase.relationship;

import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetRelationshipsById extends AbstractOrientPlugin {
  
  public GetRelationshipsById(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> relationshipIds = new ArrayList<String>();
    
    relationshipIds = (List<String>) requestMap.get("ids");
    List<Map<String, Object>> relationshipsList = new ArrayList<>();
    for (String relationshipId : relationshipIds) {
      Vertex relationshipNode = UtilClass.getVertexByIndexedId(relationshipId,
          VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
      relationshipsList.add(RelationshipUtils.getRelationshipMapWithContext(relationshipNode));
    }
    Map<String, Object> response = new HashMap<>();
    response.put("list", relationshipsList);
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRelationshipsById/*" };
  }
}
