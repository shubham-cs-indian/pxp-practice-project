package com.cs.config.strategy.plugin.usecase.boardinguser.abstrct;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractGetBoardingUser extends AbstractOrientPlugin {
  
  public AbstractGetBoardingUser(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public Map<String, Object> getBoardingUser(Map<String, Object> map) throws Exception
  {
    HashMap<String, Object> returnMap = null;
    
    String supplierName = (String) map.get("onboardingUserName");
    String endpointType = (String) map.get(IEndpoint.ENDPOINT_TYPE);
    OrientGraph graph = UtilClass.getGraph();
    
    Iterable<Vertex> resultIterable = graph
        .command(new OCommandSQL("select from " + VertexLabelConstants.ONBOARDING_USER + " where "
            + CommonConstants.USER_NAME_PROPERTY + " = '" + supplierName + "'"))
        .execute();
    Iterator<Vertex> iterator = resultIterable.iterator();
    
    Vertex supplierNode = null;
    while (iterator.hasNext()) {
      supplierNode = iterator.next();
    }
    if (supplierNode != null) {
      returnMap = new HashMap<String, Object>();
      returnMap.putAll(UtilClass.getMapFromNode(supplierNode));
      Iterator<Vertex> roleNodes = supplierNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN)
          .iterator();
      while (roleNodes.hasNext()) {
        Vertex roleNode = roleNodes.next();
        Iterable<Edge> endpointInRelationships = roleNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.HAS_ENDPOINT);
        for (Edge endpointEdge : endpointInRelationships) {
          if (!(Boolean) endpointEdge.getProperty(CommonConstants.ENDPOINT_OWNER)) {
            Vertex endpointNode = endpointEdge.getVertex(Direction.IN);
            if (endpointNode.getProperty(IEndpoint.ENDPOINT_TYPE)
                .equals(endpointType)) {
              returnMap.put(IEndpoint.INDEX_NAME,
                  (String) endpointNode.getProperty(IEndpoint.INDEX_NAME));
              break;
            }
          }
        }
      }
    }
    
    return returnMap;
  }
}
