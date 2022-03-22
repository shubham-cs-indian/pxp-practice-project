package com.cs.config.strategy.plugin.usecase.standardproperties;

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

public class GetStandardProperties extends AbstractOrientPlugin {
  
  public GetStandardProperties(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    Iterator<Vertex> propertyNodes = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_STANDARD, true)
        .iterator();
    
    if (!propertyNodes.hasNext()) {
      // throw new Exception("STANDARD_PROPERTIES_NOT_FOUND");
    }
    
    List<Map<String, Object>> propertyList = new ArrayList<>();
    while (propertyNodes.hasNext()) {
      
      Vertex propertyNode = propertyNodes.next();
      
      HashMap<String, Object> mapproperties = new HashMap<String, Object>();
      mapproperties.putAll(UtilClass.getMapFromNode(propertyNode));
      propertyList.add(mapproperties);
    }
    Map<String, Object> response = new HashMap<>();
    response.put("list", propertyList);
    
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetStandardProperties/*" };
  }
}
