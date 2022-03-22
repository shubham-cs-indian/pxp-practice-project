package com.cs.config.strategy.plugin.usecase.relationship;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.relationship.IRelationsIdsModel;
import com.cs.core.config.interactor.model.relationship.IRelationsTypesModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetNatureRelationshipByType extends AbstractOrientPlugin {
  
  public GetNatureRelationshipByType(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    List<String> natureRelationshiptypes = (List<String>) requestMap
        .get(IRelationsTypesModel.NATURE_RELATIONSHIP_TYPES);
    List<String> natureRelationshipIds = new ArrayList<>();
    
    if (natureRelationshiptypes.size() > 0) {
      String query = "select from " + VertexLabelConstants.NATURE_RELATIONSHIP
          + " where relationshipType in " + EntityUtil.quoteIt(natureRelationshiptypes);
      natureRelationshipIds = executeQueryAndPrepareResponse(query);
    }
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IRelationsIdsModel.NATURE_RELATIONSHIP_IDS, natureRelationshipIds);
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetNatureRelationshipByType/*" };
  }
  
  private List<String> executeQueryAndPrepareResponse(String query) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    List<String> natureRelationIds = new ArrayList<>();
    for (Vertex natureRelationNode : searchResults) {
      String natureRelationId = UtilClass.getCodeNew(natureRelationNode);
      natureRelationIds.add(natureRelationId);
    }
    return natureRelationIds;
  }
}
