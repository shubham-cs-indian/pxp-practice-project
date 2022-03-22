package com.cs.config.strategy.plugin.usecase.standardrole;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.HashMap;
import java.util.Map;

public class CreateStandardRole extends AbstractOrientPlugin {
  
  public CreateStandardRole(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> roleMap = (HashMap<String, Object>) requestMap.get("role");
    String roleId = roleMap.get(CommonConstants.ID_PROPERTY)
        .toString();
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_STANDARD_ROLE, CommonConstants.CODE_PROPERTY);
    Vertex roleNode = UtilClass.createNode(roleMap, vertexType);
    roleNode.setProperty(CommonConstants.CODE_PROPERTY, roleId);
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.putAll(UtilClass.getMapFromNode(roleNode));
    Map<String, Object> mapToReturn = new HashMap<String, Object>();
    mapToReturn.put("role", returnMap);
    UtilClass.getGraph()
        .commit();
    return mapToReturn;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateStandardRole/*" };
  }
}
