package com.cs.config.strategy.plugin.usecase.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class OnboardingRoleUtils {
  
  @Deprecated
  public static void createEndpointForOnboardingRole(String roleId) throws Exception
  {
    Map<String, Object> endpointMap = new HashMap<String, Object>();
    Vertex role = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ENDPOINT,
        CommonConstants.CODE_PROPERTY);
    endpointMap.put(IEndpoint.ID, UtilClass.getUniqueSequenceId(vertexType));
    endpointMap.put(IEndpoint.LABEL, UtilClass.getValueByLanguage(role, IRole.LABEL) + "_Endpoint");
    endpointMap.put(IConfigEntityInformationModel.TYPE, CommonConstants.INBOUND_ENDPOINT);
    Vertex endpointNode = UtilClass.createNode(endpointMap, vertexType, new ArrayList<String>());
    Edge edge = role.addEdge(RelationshipLabelConstants.HAS_ENDPOINT, endpointNode);
    edge.setProperty(CommonConstants.ENDPOINT_OWNER, true);
    UtilClass.getGraph()
        .commit();
  }
  
  public static void manageAddedEndpoints(List<String> addedEndpointIds, Vertex roleNode)
      throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    for (String endpointId : addedEndpointIds) {
      Vertex endpointNode = UtilClass.getVertexByIndexedId(endpointId,
          VertexLabelConstants.ENDPOINT);
      Edge edge = roleNode.addEdge(RelationshipLabelConstants.HAS_ENDPOINT, endpointNode);
      edge.setProperty(CommonConstants.ENDPOINT_OWNER, false);
    }
  }
}
