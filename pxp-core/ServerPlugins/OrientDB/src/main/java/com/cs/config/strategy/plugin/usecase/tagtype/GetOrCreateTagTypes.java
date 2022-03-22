package com.cs.config.strategy.plugin.usecase.tagtype;

import com.cs.config.strategy.plugin.usecase.tagtype.util.TagTypeUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GetOrCreateTagTypes extends AbstractOrientPlugin {
  
  public GetOrCreateTagTypes(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings({ "unchecked", "deprecation" })
  @Override
  protected Object execute(Map<String, Object> map) throws Exception
  {
    Map<String, Object> tagTypeMap;
    Map<String, Object> returnMap = new HashMap<String, Object>();
    
    List<HashMap<String, Object>> tagTypeMapList = (List<HashMap<String, Object>>) map
        .get("tagType");
    
    Iterator<Vertex> tagTypeNodes = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TAG_TYPE)
        .iterator();
    List<String> tagTypeIdsAlreadyExistingInDB = new ArrayList<>();
    while (tagTypeNodes.hasNext()) {
      Vertex tagTypeNode = tagTypeNodes.next();
      tagTypeIdsAlreadyExistingInDB.add(tagTypeNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
    
    for (int i = 0; i < tagTypeMapList.size(); i++) {
      tagTypeMap = tagTypeMapList.get(i);
      String tagTypeId = tagTypeMap.get(CommonConstants.ID_PROPERTY)
          .toString();
      if (!tagTypeIdsAlreadyExistingInDB.contains(tagTypeId)) {
        OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
            VertexLabelConstants.ENTITY_TAG_TYPE, CommonConstants.CODE_PROPERTY);
        Vertex tagTypeNode = UtilClass.createNode(tagTypeMap, vertexType);
        tagTypeNode.setProperty(CommonConstants.CODE_PROPERTY,
            tagTypeMap.get(CommonConstants.ID_PROPERTY));
        
        List<HashMap<String, Object>> tagValues = (List<HashMap<String, Object>>) tagTypeMap
            .get("tagValues");
        for (HashMap<String, Object> tagValue : tagValues) {
          OrientVertexType vertexTypeTagValue = UtilClass.getOrCreateVertexType(
              VertexLabelConstants.ENTITY_TAG_VALUE, CommonConstants.CODE_PROPERTY);
          Vertex tagValueNode = UtilClass.createNode(tagValue, vertexTypeTagValue);
          tagValueNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_TAG_VALUE_OF,
              tagTypeNode);
        }
        
        // major change to remove tag values property from tag type node
        tagTypeNode.removeProperty("tagValues");
        returnMap = TagTypeUtil.getTagTypeMap(tagTypeNode);
      }
    }
    UtilClass.getGraph()
        .commit();
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateTagTypes/*" };
  }
}
