package com.cs.config.strategy.plugin.usecase.attribute;

import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAttributesById extends AbstractOrientPlugin {
  
  public GetAttributesById(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    // CHECK THE SYNTAX. 3 IS THE NUMBER OF MANDATORY PARAMETERS
    
    List<String> attributeIds = new ArrayList<String>();
    attributeIds = (List<String>) map.get("ids");
    
    OrientGraph graph = UtilClass.getGraph();
    UtilClass.setGraph(graph);
    List<Map<String, Object>> attributesList = new ArrayList<>();
    for (String attributeId : attributeIds) {
      Vertex attributeNode = UtilClass.getVertexByIndexedId(attributeId,
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      HashMap<String, Object> attributeMap = new HashMap<String, Object>();
      attributeMap = (HashMap<String, Object>) AttributeUtils.getAttributeMap(attributeNode);
      
      attributesList.add(attributeMap);
    }
    // }
    Map<String, Object> response = new HashMap<>();
    response.put("list", attributesList);
    
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAttributesById/*" };
  }
}
