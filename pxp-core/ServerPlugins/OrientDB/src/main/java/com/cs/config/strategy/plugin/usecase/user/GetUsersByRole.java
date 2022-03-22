package com.cs.config.strategy.plugin.usecase.user;

import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.role.RoleNotFoundException;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GetUsersByRole extends AbstractOrientPlugin {
  
  public GetUsersByRole(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    List<Map<String, Object>> usersList = new ArrayList<>();
    Map<String, Object> roleMap = new HashMap<String, Object>();
    
    String roleId = (String) requestMap.get("id");
    
    OrientGraph graph = UtilClass.getGraph();
    
    Iterator<Vertex> iterator = graph
        .getVertices(CommonConstants.ROLE, new String[] { CommonConstants.CODE_PROPERTY },
            new Object[] { roleId })
        .iterator();
    Vertex roleNode = null;
    if (!iterator.hasNext()) {
      throw new RoleNotFoundException();
    }
    roleNode = iterator.next();
    
    RoleUtils.addUsersToRole(roleMap, roleNode);
    
    List users = (List) roleMap.get("users");
    roleMap.remove("users");
    roleMap.put("usersList", users);
    roleMap.put("count", users.size());
    
    return roleMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetUsersByRole/*" };
  }
}
