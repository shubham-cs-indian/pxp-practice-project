package com.cs.config.strategy.plugin.usecase.standardrole;

import com.cs.config.strategy.plugin.usecase.util.OnboardingRoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetOrCreateRole extends AbstractOrientPlugin {
  
  public GetOrCreateRole(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<HashMap<String, Object>> roles = (List<HashMap<String, Object>>) requestMap
        .get(CommonConstants.ROLE);
    Map<String, Object> mapToReturn = new HashMap<String, Object>();
    
    for (Map<String, Object> role : roles) {
      Vertex roleNode = null;
      String roleId = (String) role.get(IRole.ID);
      List<Map<String, Object>> addedUsersMap = (List<Map<String, Object>>) role
          .remove(IRole.USERS);
      List<String> addedEndpointIds = (List<String>) role.get(IRole.ENDPOINTS);
      List<String> addedUsers = new ArrayList<>();
      for (Map<String, Object> addedUserMap : addedUsersMap) {
        addedUsers.add((String) addedUserMap.get(IUser.ID));
      }
      try {
        roleNode = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
      }
      catch (NotFoundException e) {
        String label = VertexLabelConstants.ENTITY_TYPE_ROLE;
        Map<String, Object> createRequestMap = new HashMap<>();
        createRequestMap.put(CommonConstants.ROLE, role);
        OrientVertexType vertexType = UtilClass.getOrCreateVertexType(label,
            CommonConstants.CODE_PROPERTY);
        roleNode = UtilClass.createNode(role, vertexType, new ArrayList<>());
        manageAddedUsers(addedUsers, roleNode, VertexLabelConstants.ENTITY_TYPE_USER);
        // OnboardingRoleUtils.createEndpointForOnboardingRole((String)
        // roleNode.getProperty("cid"));
        OnboardingRoleUtils.manageAddedEndpoints(addedEndpointIds, roleNode);
        
        HashMap<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.putAll(UtilClass.getMapFromNode(roleNode));
        mapToReturn.put("role", returnMap);
      }
    }
    
    UtilClass.getGraph()
        .commit();
    return mapToReturn;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateRole/*" };
  }
  
  private void manageAddedUsers(List<String> userIds, Vertex roleNode, String label)
  {
    OrientGraph graph = UtilClass.getGraph();
    for (String userId : userIds) {
      Vertex userNode = graph
          .getVertices(label, new String[] { CommonConstants.CODE_PROPERTY },
              new Object[] { userId })
          .iterator()
          .next();
      userNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN, roleNode);
    }
  }
}
