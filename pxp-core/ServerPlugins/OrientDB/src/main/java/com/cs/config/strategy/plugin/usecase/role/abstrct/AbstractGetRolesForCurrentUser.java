package com.cs.config.strategy.plugin.usecase.role.abstrct;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.user.UserNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractGetRolesForCurrentUser extends AbstractOrientPlugin {
  
  public AbstractGetRolesForCurrentUser(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public void getRolesForCurrentUser(Map<String, Object> requestMap, String userId,
      HashMap<String, Object> returnMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    try {
      Vertex userNode = UtilClass.getVertexByIndexedId(userId,
          VertexLabelConstants.ENTITY_TYPE_USER);
      getRoleIdsForCurrentUser(userNode, returnMap);
    }
    catch (NotFoundException e) {
      throw new UserNotFoundException();
    }
  }
  
  private void getRoleIdsForCurrentUser(Vertex userNode, HashMap<String, Object> returnMap)
  {
    Iterable<Vertex> roleNodes = userNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN);
    Set<String> roleIds = new HashSet<String>();
    for (Vertex roleNode : roleNodes) {
      roleIds.add((String) roleNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
    returnMap.put("ids", roleIds);
  }
}
