package com.cs.config.strategy.plugin.usecase.standardattribute;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetOrCreateAttribute extends AbstractOrientPlugin {
  
  public GetOrCreateAttribute(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    HashMap<String, Object> attributeMap = new HashMap<String, Object>();
    List<HashMap<String, Object>> attributeMapList = new ArrayList<HashMap<String, Object>>();
    
    Map<String, Object> returnMap = new HashMap<String, Object>();
    List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
    
    attributeMapList = (List<HashMap<String, Object>>) requestMap.get("attribute");
    
    // Iterator<Vertex> stdAttributeNodes = UtilClass.getGraph()
    // .getVerticesOfClass(VertexLabelConstants.ENTITY_STANDARD_ATTRIBUTE,
    // false).iterator();
    
    for (int i = 0; i < attributeMapList.size(); i++) {
      attributeMap = attributeMapList.get(i);
      
      try {
        UtilClass.getVertexByIndexedId((String) attributeMap.get(CommonConstants.ID_PROPERTY),
            VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      }
      catch (NotFoundException e) {
        OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
            VertexLabelConstants.ENTITY_STANDARD_ATTRIBUTE, CommonConstants.CODE_PROPERTY);
        
        String attributeId = attributeMap.get(CommonConstants.ID_PROPERTY)
            .toString();
        
        Vertex attributeNode = UtilClass.createNode(attributeMap, vertexType, new ArrayList<>());
        attributeNode.setProperty(CommonConstants.CODE_PROPERTY, attributeId);
        
        // returnMap.putAll(UtilClass.getMapFromNode(attributeNode));
        returnList.add(UtilClass.getMapFromNode(attributeNode));
      }
    }
    UtilClass.getGraph()
        .commit();
    
    returnMap.put(IListModel.LIST, returnList);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateAttribute/*" };
  }
}
