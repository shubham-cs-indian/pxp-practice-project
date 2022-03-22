package com.cs.config.strategy.plugin.usecase.relationship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class GetAllNatureRelationshipIds extends AbstractOrientPlugin {
  
  public GetAllNatureRelationshipIds(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
 
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String query = "select from " + VertexLabelConstants.NATURE_RELATIONSHIP;
    List<String> natureRelationshipIds = executeQueryAndPrepareResponse(query);
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IIdsListParameterModel.IDS, natureRelationshipIds);
    
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllNatureRelationshipIds/*" };
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
