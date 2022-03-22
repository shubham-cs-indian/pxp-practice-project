package com.cs.config.strategy.plugin.usecase.standardattribute;

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

public class GetStandardAttributes extends AbstractOrientPlugin {
  
  public GetStandardAttributes(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Iterator<Vertex> attributeNodes = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_STANDARD_ATTRIBUTE, false)
        .iterator();
    
    if (!attributeNodes.hasNext()) {
      // throw new Exception("STANDARD_ATTRIBUTES_NOT_FOUND");
      
    }
    
    List<Map<String, Object>> attributesList = new ArrayList<>();
    
    while (attributeNodes.hasNext()) {
      
      Vertex attributeNode = attributeNodes.next();
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.putAll(UtilClass.getMapFromNode(attributeNode));
      attributesList.add(map);
    }
    Map<String, Object> response = new HashMap<String, Object>();
    response.put("list", attributesList);
    
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetStandardAttributes/*" };
  }
}
