package com.cs.config.strategy.plugin.usecase.mapping;

import com.cs.config.strategy.plugin.usecase.mapping.util.MappingUtils;
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

@SuppressWarnings({ "unchecked" })
public class DeleteMapping extends AbstractOrientPlugin {
  
  public DeleteMapping(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    List<String> profileIds = (List<String>) map.get("ids");
    OrientGraph graph = UtilClass.getGraph();
    
    List<String> deletedIds = new ArrayList<>();
    for (String id : profileIds) {
      Vertex profileNode = UtilClass.getVertexByIndexedId(id,
          VertexLabelConstants.PROPERTY_MAPPING);
      if (profileNode != null) {
        MappingUtils.deleteEndpointMappings(profileNode);
        profileNode.remove();
      }
      deletedIds.add(id);
    }
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("success", deletedIds);
    
    graph.commit();
    
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteMapping/*" };
  }
}
