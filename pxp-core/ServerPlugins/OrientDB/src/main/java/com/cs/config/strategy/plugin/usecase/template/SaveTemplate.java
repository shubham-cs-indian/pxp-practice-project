package com.cs.config.strategy.plugin.usecase.template;

import com.cs.config.strategy.plugin.usecase.template.util.CustomTemplateUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.context.ContextNotFoundException;
import com.cs.core.config.interactor.exception.propertycollection.PropertyCollectionNotFoundException;
import com.cs.core.config.interactor.exception.relationship.RelationshipNotFoundException;
import com.cs.core.config.interactor.exception.template.TemplateNotFoundException;
import com.cs.core.config.interactor.model.customtemplate.ISaveCustomTemplateModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.template.ISaveTemplateModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SaveTemplate extends AbstractOrientPlugin {
  
  protected List<String> fieldsToExclude = Arrays.asList(
      ISaveCustomTemplateModel.ADDED_PROPERTYCOLLECTIONS,
      ISaveCustomTemplateModel.DELETED_PROPERTYCOLLECTIONS,
      ISaveCustomTemplateModel.ADDED_RELATIONSHIPS, ISaveCustomTemplateModel.DELETED_RELATIONSHIPS,
      ISaveCustomTemplateModel.ADDED_NATURE_RELATIONSHIPS,
      ISaveCustomTemplateModel.DELETED_NATURE_RELATIONSHIPS,
      ISaveCustomTemplateModel.DELETED_CONTEXTS, ISaveCustomTemplateModel.ADDED_CONTEXTS);
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveTemplate/*" };
  }
  
  public SaveTemplate(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> saveTemplateMap = (Map<String, Object>) requestMap.get("template");
    String temaplateId = (String) saveTemplateMap.get(ISaveTemplateModel.ID);
    Vertex templateNode = null;
    try {
      templateNode = UtilClass.getVertexById(temaplateId, VertexLabelConstants.TEMPLATE);
    }
    catch (NotFoundException e) {
      throw new TemplateNotFoundException(e);
    }
    
    manageAddedPropertyCollections(templateNode, saveTemplateMap);
    manageDeletedPropertyCollections(templateNode, saveTemplateMap);
    manageAddedRelationships(templateNode, saveTemplateMap);
    manageDeletedRelationships(templateNode, saveTemplateMap);
    manageAddedNatureRelationships(templateNode, saveTemplateMap);
    manageDeletedNatureRelationships(templateNode, saveTemplateMap);
    manageAddedContexts(templateNode, saveTemplateMap);
    manageDeletedContexts(templateNode, saveTemplateMap);
    
    UtilClass.saveNode(saveTemplateMap, templateNode, fieldsToExclude);
    UtilClass.getGraph()
        .commit();
    return CustomTemplateUtil.prepareTemplateResponseMap(templateNode);
  }
  
  private void manageAddedContexts(Vertex templateNode, Map<String, Object> saveTemplateMap)
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
  
  private void manageAddedNatureRelationships(Vertex templateNode,
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
  
  private void manageDeletedContexts(Vertex templateNode, Map<String, Object> saveTemplateMap)
      throws Exception
  {
    List<String> deletedContextIds = (List<String>) saveTemplateMap
        .get(ISaveCustomTemplateModel.DELETED_CONTEXTS);
    removePropertiesFromTemplate(templateNode, deletedContextIds,
        RelationshipLabelConstants.HAS_TEMPLATE_CONTEXT);
  }
  
  private void manageDeletedRelationships(Vertex templateNode, Map<String, Object> saveTemplateMap)
      throws Exception
  {
    List<String> deletedRelationshipIds = (List<String>) saveTemplateMap
        .get(ISaveCustomTemplateModel.DELETED_RELATIONSHIPS);
    removePropertiesFromTemplate(templateNode, deletedRelationshipIds,
        RelationshipLabelConstants.HAS_TEMPLATE_RELATIONSHIP);
  }
  
  private void manageDeletedNatureRelationships(Vertex templateNode,
      Map<String, Object> saveTemplateMap) throws Exception
  {
    List<String> deletedNatureRelationshipIds = (List<String>) saveTemplateMap
        .get(ISaveCustomTemplateModel.DELETED_NATURE_RELATIONSHIPS);
    removePropertiesFromTemplate(templateNode, deletedNatureRelationshipIds,
        RelationshipLabelConstants.HAS_TEMPLATE_NATURE_RELATIONSHIP);
  }
  
  private void manageDeletedPropertyCollections(Vertex templateNode,
      Map<String, Object> saveTemplateMap) throws Exception
  {
    List<String> deletedPropertyCollectionIds = (List<String>) saveTemplateMap
        .get(ISaveCustomTemplateModel.DELETED_PROPERTYCOLLECTIONS);
    removePropertiesFromTemplate(templateNode, deletedPropertyCollectionIds,
        RelationshipLabelConstants.HAS_TEMPLATE_PROPERTY_COLLECTION);
  }
  
  private void removePropertiesFromTemplate(Vertex templateNode, List<String> propertyIds,
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
