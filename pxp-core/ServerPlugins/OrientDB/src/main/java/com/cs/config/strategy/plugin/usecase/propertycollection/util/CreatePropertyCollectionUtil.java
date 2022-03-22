package com.cs.config.strategy.plugin.usecase.propertycollection.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollectionElement;
import com.cs.core.config.interactor.model.propertycollection.ICreatePropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.IPropertyCollectionModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public class CreatePropertyCollectionUtil {
  
  public static final List<String> fieldsToExclude = Arrays
      .asList(IPropertyCollectionModel.ELEMENTS, ICreatePropertyCollectionModel.TAB);
  
  public static Vertex createPropertyCollection(Map<String, Object> propertyCollectionMap)
      throws Exception
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.PROPERTY_COLLECTION, CommonConstants.CODE_PROPERTY);
    Vertex propertyCollectionNode = UtilClass.createNode(propertyCollectionMap, vertexType,
        fieldsToExclude);
    
    List<String> attributeIds = new ArrayList<>();
    List<String> tagIds = new ArrayList<>();
    List<Map<String, Object>> elements = (List<Map<String, Object>>) propertyCollectionMap
        .get(IPropertyCollectionModel.ELEMENTS);
    for (Map<String, Object> element : elements) {
      addElement(propertyCollectionNode, attributeIds, tagIds, element);
    }
    
    propertyCollectionNode.setProperty(IPropertyCollection.ATTRIBUTE_IDS, attributeIds);
    propertyCollectionNode.setProperty(IPropertyCollection.TAG_IDS, tagIds);
    
    // tab
    Map<String, Object> tabMap = (Map<String, Object>) propertyCollectionMap
        .get(ICreatePropertyCollectionModel.TAB);
    TabUtils.linkAddedOrDefaultTab(propertyCollectionNode, tabMap,
        CommonConstants.PROPERTY_COLLECTION);
    
    UtilClass.getGraph()
        .commit();
    return propertyCollectionNode;
  }

  public static String addElement(Vertex propertyCollectionNode, List<String> attributeIds, List<String> tagIds, Map<String, Object> element)
      throws Exception
  {
    String elementId = (String) element.get(IPropertyCollectionElement.ID);
    String elementType = (String) element.get(IPropertyCollectionElement.TYPE);
    Vertex elementNode = PropertyCollectionUtils.getElementVertexFromIdAndType(elementId,
        elementType);
    Edge entityTo = elementNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO,
        propertyCollectionNode);
    entityTo.setProperty(CommonConstants.TYPE_PROPERTY, elementType);
    
    if (elementType.equals(CommonConstants.ATTRIBUTE_PROPERTY)) {
      attributeIds.add(elementId);
    }
    else if (elementType.equals(CommonConstants.TAG_PROPERTY)) {
      tagIds.add(elementId);
    }
    return elementId;
  }
}
