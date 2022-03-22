package com.cs.config.strategy.plugin.usecase.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.mapping.util.OutboundMappingUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings({ "unchecked" })
public class DeleteOutboundMapping extends AbstractOrientPlugin {
  
  public DeleteOutboundMapping(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    List<String> profileIds = (List<String>) map.get(IIdsListParameterModel.IDS);
    OrientGraph graph = UtilClass.getGraph();
    
    List<String> deletedIds = new ArrayList<>();
    for (String id : profileIds) {
      Vertex profileNode = UtilClass.getVertexByIndexedId(id,
          VertexLabelConstants.PROPERTY_MAPPING);
      if (profileNode != null) {
        OutboundMappingUtils.deleteEndpointMappings(profileNode);
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
    return new String[] { "POST|DeleteOutboundMapping/*" };
  }
  
}
