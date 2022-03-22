package com.cs.config.strategy.plugin.usecase.standardrole;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GetStandardRoles extends AbstractOrientPlugin {
  
  public GetStandardRoles(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Iterator<Vertex> roleNodes = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_STANDARD_ROLE, false)
        .iterator();
    
    if (!roleNodes.hasNext()) {
      // throw new Exception("STANDARD_PROPERTIES_NOT_FOUND");
    }
    
    List<Map<String, Object>> attributesList = new ArrayList<>();
    while (roleNodes.hasNext()) {
      
      Vertex roleNode = roleNodes.next();
      Map<String, Object> map = new HashMap<String, Object>();
      map.putAll(UtilClass.getMapFromNode(roleNode));
      
      attributesList.add(map);
    }
    
    Map<String, Object> response = new HashMap<String, Object>();
    response.put("list", attributesList);
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetStandardRoles/*" };
  }
}
