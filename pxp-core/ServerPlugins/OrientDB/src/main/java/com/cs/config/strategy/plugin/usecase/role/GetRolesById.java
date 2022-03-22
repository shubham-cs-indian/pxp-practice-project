package com.cs.config.strategy.plugin.usecase.role;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetRolesById extends AbstractOrientPlugin {
  
  public GetRolesById(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    
    List<String> roleIds = new ArrayList<String>();
    
    roleIds = (List<String>) map.get("ids");
    
    OrientGraph graph = UtilClass.getGraph();
    
    List<Map<String, Object>> rolesList = new ArrayList<>();
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY, IRole.LABEL);
    for (String roleId : roleIds) {
      Vertex roleNode = UtilClass.getVertexByIndexedId(roleId,
          VertexLabelConstants.ENTITY_TYPE_ROLE);
      rolesList.add(UtilClass.getMapFromVertex(fieldsToFetch, roleNode));
    }
    Map<String, Object> response = new HashMap<>();
    response.put("list", rolesList);
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRolesById/*" };
  }
}
