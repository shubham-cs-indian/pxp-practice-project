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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BulkGetMapping extends AbstractOrientPlugin {
  
  public BulkGetMapping(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    HashMap<String, Object> mapToReturn = new HashMap<String, Object>();
    List<HashMap<String, Object>> returnList = new ArrayList<>();
    
    OrientGraph graph = UtilClass.getGraph();
    Iterator<Vertex> iterator = graph
        .getVerticesOfClass(VertexLabelConstants.PROPERTY_MAPPING, false)
        .iterator();
    Map<String, Object> attributeMapForIdAndLabel = new HashMap<>();
    Map<String, Object> tagMapForIdAndLabel = new HashMap<>();
    Map<String, Object> taxonomyMapForIdAndLabel = new HashMap<>();
    Map<String, Object> klassMapForIdAndLabel = new HashMap<>();
    Map<String, Object> relationshipMapForIdAndLabel = new HashMap<>();

    while (iterator.hasNext()) {
      HashMap<String, Object> returnMap = new HashMap<String, Object>();
      Vertex profileNode = iterator.next();
      returnMap.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), profileNode));
      MappingUtils.getMapFromProfileNode(profileNode, returnMap, new ArrayList<>(),
          attributeMapForIdAndLabel, tagMapForIdAndLabel, taxonomyMapForIdAndLabel,
          klassMapForIdAndLabel, relationshipMapForIdAndLabel);
      returnList.add(returnMap);
    }
    mapToReturn.put("list", returnList);
    return mapToReturn;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkGetMapping/*" };
  }
}
