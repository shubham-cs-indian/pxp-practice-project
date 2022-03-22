package com.cs.config.strategy.plugin.usecase.tagtype.util;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagTypeUtil {
  
  public static Map<String, Object> getTagTypeMap(Vertex tagTypeNode)
  {
    Map<String, Object> tagTypeMap = new HashMap<>();
    tagTypeMap.putAll(UtilClass.getMapFromNode(tagTypeNode));
    Iterable<Edge> tagValuesOfRelationship = tagTypeNode.getEdges(
        com.tinkerpop.blueprints.Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_TAG_VALUE_OF);
    
    List<Map<String, Object>> tagValues = new ArrayList<>();
    tagTypeMap.put(CommonConstants.TAG_VALUES, tagValues);
    
    for (Edge tagValueRelationship : tagValuesOfRelationship) {
      Map<String, Object> tagValueDataMap = new HashMap<>();
      Vertex tagValueNode = tagValueRelationship.getVertex(com.tinkerpop.blueprints.Direction.OUT);
      tagValueDataMap.putAll(UtilClass.getMapFromNode(tagValueNode));
      tagValues.add(tagValueDataMap);
    }
    return tagTypeMap;
  }
}
