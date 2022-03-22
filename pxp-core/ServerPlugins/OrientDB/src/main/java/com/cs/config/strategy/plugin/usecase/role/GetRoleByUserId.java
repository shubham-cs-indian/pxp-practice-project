package com.cs.config.strategy.plugin.usecase.role;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetRoleByUserId extends AbstractOrientPlugin {
  
  public GetRoleByUserId(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> map) throws Exception
  {
    
    String userId = (String) map.get("userId");
    
    List<String> rolesList = new ArrayList<>();
    Map<String, Object> response = new HashMap<>();
    /*   List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY);*/
    Vertex user = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
    Iterable<Vertex> roles = user.getVertices(Direction.OUT, "User_In");
    for (Vertex role : roles) {
      String code = UtilClass.getCodeNew(role);
      rolesList.add(code);
    }
    response.put("ids", rolesList);
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRoleByUserId/*" };
  }
}
