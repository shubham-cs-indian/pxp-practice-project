package com.cs.config.strategy.plugin.usecase.template.util;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.context.ContextNotFoundException;
import com.cs.core.config.interactor.exception.propertycollection.PropertyCollectionNotFoundException;
import com.cs.core.config.interactor.exception.relationship.RelationshipNotFoundException;
import com.cs.core.config.interactor.model.customtemplate.ISaveCustomTemplateModel;
import com.cs.core.config.interactor.model.template.ISaveTemplateModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class TemplateUtils {
  
  public static void manageAddedContexts(Vertex templateNode, Map<String, Object> saveTemplateMap)
      throws Exception
  {
    List<String> addedContexts = (List<String>) saveTemplateMap
        .get(ISaveCustomTemplateModel.ADDED_CONTEXTS);
    for (String contextId : addedContexts) {
      Vertex relationshipNode = null;
      try {
        relationshipNode = UtilClass.getVertexById(contextId, VertexLabelConstants.VARIANT_CONTEXT);
      }
      catch (NotFoundException e) {
        throw new ContextNotFoundException();
      }
      templateNode.addEdge(RelationshipLabelConstants.HAS_TEMPLATE_CONTEXT, relationshipNode);
    }
  }
  
  public static void manageAddedRelationships(Vertex templateNode,
      Map<String, Object> saveTemplateMap) throws Exception
  {
    List<String> addedRelationships = (List<String>) saveTemplateMap
        .get(ISaveTemplateModel.ADDED_RELATIONSHIPS);
    for (String relationshipId : addedRelationships) {
      Vertex relationshipNode = null;
      try {
        relationshipNode = UtilClass.getVertexById(relationshipId,
            VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
      }
      catch (NotFoundException e) {
        throw new RelationshipNotFoundException();
      }
      templateNode.addEdge(RelationshipLabelConstants.HAS_TEMPLATE_RELATIONSHIP, relationshipNode);
    }
  }
  
  public static void manageAddedPropertyCollections(Vertex templateNode,
      Map<String, Object> saveTemplateMap) throws Exception
  {
    List<String> addedPropertyCollectionIds = (List<String>) saveTemplateMap
        .get(ISaveTemplateModel.ADDED_PROPERTYCOLLECTIONS);
    for (String propertyCollectionId : addedPropertyCollectionIds) {
      Vertex propertyCollectionNode = null;
      try {
        propertyCollectionNode = UtilClass.getVertexById(propertyCollectionId,
            VertexLabelConstants.PROPERTY_COLLECTION);
      }
      catch (NotFoundException e) {
        throw new PropertyCollectionNotFoundException();
      }
      templateNode.addEdge(RelationshipLabelConstants.HAS_TEMPLATE_PROPERTY_COLLECTION,
          propertyCollectionNode);
    }
  }
  
  public static void manageAddedNatureRelationships(Vertex templateNode,
      Map<String, Object> saveTemplateMap) throws Exception
  {
    List<String> addedNatureRelationships = (List<String>) saveTemplateMap
        .get(ISaveCustomTemplateModel.ADDED_NATURE_RELATIONSHIPS);
    for (String relationshipId : addedNatureRelationships) {
      Vertex relationshipNode = null;
      try {
        relationshipNode = UtilClass.getVertexById(relationshipId,
            VertexLabelConstants.NATURE_RELATIONSHIP);
      }
      catch (NotFoundException e) {
        throw new RelationshipNotFoundException();
      }
      templateNode.addEdge(RelationshipLabelConstants.HAS_TEMPLATE_NATURE_RELATIONSHIP,
          relationshipNode);
    }
  }
  
  public static void manageDeletedContexts(Vertex templateNode, Map<String, Object> saveTemplateMap)
      throws Exception
  {
    List<String> deletedContextIds = (List<String>) saveTemplateMap
        .get(ISaveCustomTemplateModel.DELETED_CONTEXTS);
    removePropertiesFromTemplate(templateNode, deletedContextIds,
        RelationshipLabelConstants.HAS_TEMPLATE_CONTEXT);
  }
  
  public static void manageDeletedRelationships(Vertex templateNode,
      Map<String, Object> saveTemplateMap) throws Exception
  {
    List<String> deletedRelationshipIds = (List<String>) saveTemplateMap
        .get(ISaveCustomTemplateModel.DELETED_RELATIONSHIPS);
    removePropertiesFromTemplate(templateNode, deletedRelationshipIds,
        RelationshipLabelConstants.HAS_TEMPLATE_RELATIONSHIP);
  }
  
  public static void manageDeletedNatureRelationships(Vertex templateNode,
      Map<String, Object> saveTemplateMap) throws Exception
  {
    List<String> deletedNatureRelationshipIds = (List<String>) saveTemplateMap
        .get(ISaveCustomTemplateModel.DELETED_NATURE_RELATIONSHIPS);
    removePropertiesFromTemplate(templateNode, deletedNatureRelationshipIds,
        RelationshipLabelConstants.HAS_TEMPLATE_NATURE_RELATIONSHIP);
  }
  
  public static void manageDeletedPropertyCollections(Vertex templateNode,
      Map<String, Object> saveTemplateMap) throws Exception
  {
    List<String> deletedPropertyCollectionIds = (List<String>) saveTemplateMap
        .get(ISaveCustomTemplateModel.DELETED_PROPERTYCOLLECTIONS);
    removePropertiesFromTemplate(templateNode, deletedPropertyCollectionIds,
        RelationshipLabelConstants.HAS_TEMPLATE_PROPERTY_COLLECTION);
  }
  
  private static void removePropertiesFromTemplate(Vertex templateNode, List<String> propertyIds,
      String label)
  {
    if (propertyIds.isEmpty()) {
      return;
    }
    Iterable<Edge> edges = templateNode.getEdges(Direction.OUT, label);
    for (Edge edge : edges) {
      Vertex vertex = edge.getVertex(Direction.IN);
      if (propertyIds.contains(UtilClass.getCodeNew(vertex))) {
        edge.remove();
      }
    }
  }
}
