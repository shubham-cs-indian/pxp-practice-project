package com.cs.config.strategy.plugin.usecase.propertycollection.util;

import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollectionElement;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.attribute.AttributeNotFoundException;
import com.cs.core.config.interactor.exception.relationship.RelationshipNotFoundException;
import com.cs.core.config.interactor.exception.role.RoleNotFoundException;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.propertycollection.IGetPropertyCollectionModel;
import com.cs.core.exception.MultipleLinkFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PropertyCollectionUtils {
  
  public static final List<String> TAXONOMY_PROPERTIES_TO_FETCH = Arrays.asList(
      IMasterTaxonomy.LABEL, IMasterTaxonomy.TYPE, IMasterTaxonomy.ICON, ITaxonomy.CODE,
      IMasterTaxonomy.TAXONOMY_TYPE);
  
  public static Map<String, Object> getPropertyCollection(Vertex propertyCollectionNode)
  {
    Map<String, Object> returnMap = UtilClass.getMapFromNode(propertyCollectionNode);
    Iterator<Edge> iterator = propertyCollectionNode
        .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO)
        .iterator();
    List<Map<String, Object>> referencedAttributes = new ArrayList<>();
    List<Map<String, Object>> referencedTags = new ArrayList<>();
    List<Map<String, Object>> referencedRoles = new ArrayList<>();
    List<Map<String, Object>> referencedRelationships = new ArrayList<>();
    List<Map<String, Object>> referencedTaxonomies = new ArrayList<>();
    List<Map<String, Object>> elements = new ArrayList<>();
    while (iterator.hasNext()) {
      Edge entityTo = iterator.next();
      String entityType = entityTo.getProperty(CommonConstants.TYPE_PROPERTY);
      Vertex entityNode = entityTo.getVertex(Direction.OUT);
      switch (entityType) {
        case CommonConstants.ATTRIBUTE_PROPERTY:
          referencedAttributes.add(UtilClass.getMapFromNode(entityNode));
          break;
        case CommonConstants.TAG_PROPERTY:
          referencedTags.add(UtilClass.getMapFromNode(entityNode));
          break;
        case CommonConstants.ROLE_PROPERTY:
          referencedRoles.add(UtilClass.getMapFromNode(entityNode));
          break;
        case CommonConstants.RELATIONSHIP_PROPERTY:
          referencedRelationships.add(UtilClass.getMapFromNode(entityNode));
          break;
        case CommonConstants.TAXONOMY_PROPERTY:
          referencedTaxonomies
              .add(UtilClass.getMapFromVertex(TAXONOMY_PROPERTIES_TO_FETCH, entityNode));
          break;
      }
      Map<String, Object> elementMap = new HashMap<>();
      elementMap.put("id", entityNode.getProperty(CommonConstants.CODE_PROPERTY));
      elementMap.put("type", entityType);
      elementMap.put("code", entityNode.getProperty(CommonConstants.CODE_PROPERTY));
      elements.add(elementMap);
    }
    
    getContainingComplexAttributes(returnMap, propertyCollectionNode);
    
    List<String> propertySequenceIds = (List<String>) returnMap.get(CommonConstants.PROPERTY_SEQUENCE_IDS);
    sortPropertyCollectionList(elements, propertySequenceIds,CommonConstants.ID_PROPERTY);
    returnMap.remove(CommonConstants.PROPERTY_SEQUENCE_IDS);
    
    returnMap.put(CommonConstants.ELEMENTS_PROPERTY, elements);
    returnMap.put(IGetPropertyCollectionModel.REFERENCED_ATTRIBUTES, referencedAttributes);
    returnMap.put(IGetPropertyCollectionModel.REFERENCED_TAGS, referencedTags);
    returnMap.put(IGetPropertyCollectionModel.REFERENCED_ROLES, referencedRoles);
    returnMap.put(IGetPropertyCollectionModel.REFERENCED_RELATIONSHIPS, referencedRelationships);
    returnMap.put(IGetPropertyCollectionModel.REFERENCED_TAXONOMIES, referencedTaxonomies);
    return returnMap;
  }
  
  private static void getContainingComplexAttributes(Map<String, Object> returnMap,
      Vertex propertyCollectionNode)
  {
    List<String> complexAttributesList = new ArrayList<>();
    Iterable<Vertex> complexAttributes = propertyCollectionNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.CONTAINS_COMPLEX_ATTRIBUTES);
    for (Vertex complexAttribute : complexAttributes) {
      complexAttributesList.add(complexAttribute.getProperty(CommonConstants.CODE_PROPERTY));
    }
    returnMap.put(IGetPropertyCollectionModel.COMPLEX_ATTRIBUTE_IDS, complexAttributesList);
  }
  
  public static Vertex getElementVertexFromIdAndType(String elementId, String elementType)
      throws Exception
  {
    Vertex elementNode = null;
    switch (elementType) {
      case CommonConstants.ATTRIBUTE_PROPERTY:
        try {
          elementNode = UtilClass.getVertexById(elementId,
              VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
        }
        catch (NotFoundException e) {
          throw new AttributeNotFoundException(elementId);
        }
        break;
      
      case CommonConstants.TAG_PROPERTY:
        try {
          elementNode = UtilClass.getVertexById(elementId, VertexLabelConstants.ENTITY_TAG);
        }
        catch (NotFoundException e) {
          throw new TagNotFoundException(elementId);
        }
        break;
      
      case CommonConstants.ROLE_PROPERTY:
        try {
          elementNode = UtilClass.getVertexById(elementId, VertexLabelConstants.ENTITY_TYPE_ROLE);
        }
        catch (NotFoundException e) {
          throw new RoleNotFoundException();
        }
        break;
      
      case CommonConstants.RELATIONSHIP_PROPERTY:
        try {
          elementNode = UtilClass.getVertexById(elementId,
              VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
        }
        catch (NotFoundException e) {
          throw new RelationshipNotFoundException();
        }
        break;
      
      case CommonConstants.TAXONOMY_PROPERTY:
        try {
          elementNode = UtilClass.getVertexById(elementId,
              VertexLabelConstants.ROOT_KLASS_TAXONOMY);
        }
        catch (NotFoundException e) {
          throw new KlassTaxonomyNotFoundException();
        }
        break;
      
      default:
        break;
    }
    return elementNode;
  }
  
  public static Edge getEntityToEdgeFromElementAndPropertyCollection(Vertex propertyCollectionNode,
      Vertex elementNode) throws NotFoundException, MultipleLinkFoundException
  {
    String query = "SELECT FROM ENTITY_TO  WHERE in = " + propertyCollectionNode.getId()
        + " and out = " + elementNode.getId();
    Iterable<Edge> entityToEdges = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Edge> iterator = entityToEdges.iterator();
    if (!iterator.hasNext()) {
      throw new NotFoundException();
    }
    
    Edge entityTo = iterator.next();
    
    if (iterator.hasNext()) {
      throw new MultipleLinkFoundException();
    }
    return entityTo;
  }
  
  public static Edge getLinkBetweenKlassAndKlassProperty(Vertex klassNode, Vertex klassPropertyNode)
  {
    String klassPropertyid = klassPropertyNode.getProperty(CommonConstants.CODE_PROPERTY);
    String getQuery = "SELECT FROM (SELECT EXPAND(bothe('"
        + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "')) FROM " + klassNode.getId()
        + ") WHERE in.code = '" + klassPropertyid + "'";
    Iterable<Edge> iterable = UtilClass.getGraph()
        .command(new OCommandSQL(getQuery))
        .execute();
    Edge hasKlassProperty = null;
    for (Edge edge : iterable) {
      hasKlassProperty = edge;
    }
    return hasKlassProperty;
  }
  
  public static Map<String, Object> prepareExportPropertyCollection(Vertex propertyCollection) throws Exception
  {
    Map<String, Object> returnMap = UtilClass.getMapFromNode(propertyCollection);
    List<String> sequence = (List<String>) returnMap.get(IPropertyCollection.PROPERTY_SEQUENCE_IDS);
    Iterator<Edge> iterator = propertyCollection
        .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO)
        .iterator();
    List<Map<String, Object>> elements = new ArrayList<>();
    while (iterator.hasNext()) {
      Edge entityTo = iterator.next();
      String entityType = entityTo.getProperty(CommonConstants.TYPE_PROPERTY);
      Vertex entityNode = entityTo.getVertex(Direction.OUT);
      Map<String, Object> elementMap = new HashMap<>();
      String code = entityNode.getProperty(CommonConstants.CODE_PROPERTY);
      elementMap.put(IPropertyCollectionElement.ID, code);
      elementMap.put(IPropertyCollectionElement.TYPE, entityType);
      elementMap.put(IPropertyCollectionElement.CODE, code);
      elementMap.put(IPropertyCollectionElement.INDEX, sequence.indexOf(code) + 1);
      elements.add(elementMap);
    }
    
    returnMap.put(CommonConstants.ELEMENTS_PROPERTY, elements);
    returnMap.remove(IPropertyCollection.ATTRIBUTE_IDS);
    returnMap.remove(IPropertyCollection.TAG_IDS);
    //tab info
    Vertex tabNode = TabUtils.getTabNode(propertyCollection);
    String tabCode = tabNode.getProperty(CommonConstants.CODE_PROPERTY);
    returnMap.put(ConfigTag.tab.name(), tabCode);
    
    return returnMap;
  }
  
  public static void sortPropertyCollectionList(List<Map<String, Object>> elements, List<String> propertySequenceIds, String sortKey) {
    Map<String,Map<String,Object>> tempMap = new HashMap<>();
    
    for (Map<String, Object> element : elements) {
      tempMap.put((String) element.get(sortKey), element);
    }
    
    elements.clear();
    
    for(String propertyId : propertySequenceIds) {
      if(tempMap.get(propertyId)!=null) {
        elements.add(tempMap.get(propertyId));
      }
    }
  }
}
