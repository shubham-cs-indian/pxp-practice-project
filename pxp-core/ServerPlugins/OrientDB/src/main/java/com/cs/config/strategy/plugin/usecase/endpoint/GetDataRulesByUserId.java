package com.cs.config.strategy.plugin.usecase.endpoint;

import com.cs.config.strategy.plugin.usecase.datarule.util.GetDataRuleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IGetMappedEndpointRequestModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetDataRulesByUserId extends AbstractOrientPlugin {
  
  public GetDataRulesByUserId(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> dataMapToReturn = new HashMap<>();
    List<Vertex> roles = new ArrayList<>();
    List<Vertex> endpoints = new ArrayList<>();
    List<Map<String, Object>> dataRules = new ArrayList<>();
    String userId = (String) requestMap.get(IGetMappedEndpointRequestModel.CURRENT_USER_ID);
    String roleId = (String) requestMap.get("roleId");
    if (userId == null) {
      roles.add(UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE));
    }
    else {
      Vertex userNode = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
      Iterator<Vertex> iterator = (Iterator<Vertex>) userNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN);
      while (iterator.hasNext()) {
        roles.add(iterator.next());
      }
    }
    for (Vertex role : roles) {
      Iterable<Edge> endPointsEdge = role.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_ENDPOINT);
      for (Edge endPointEdge : endPointsEdge) {
        Vertex endpoint = endPointEdge.getVertex(Direction.IN);
        endpoints.add(endpoint);
      }
    }
    
    for (Vertex endpoint : endpoints) {
      Iterable<Edge> dataRulesEdge = endpoint.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_DATA_RULE);
      for (Edge dataRuleEdge : dataRulesEdge) {
        Vertex datarule = dataRuleEdge.getVertex(Direction.IN);
        dataRules.add(GetDataRuleUtils.getDataRuleFromNode(datarule));
      }
    }
    dataMapToReturn.put(IListModel.LIST, dataRules);
    return dataMapToReturn;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetDataRulesByUserId/*" };
  }
}
