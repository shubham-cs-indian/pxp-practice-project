package com.cs.config.strategy.plugin.migration;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ScriptToRemoveDeprecatedFields extends AbstractOrientPlugin {
  
  public ScriptToRemoveDeprecatedFields(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|RemoveDeprecatedFields/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    
    removeExtensionRelatedFields(graph);
    removeEventAndReferencesRelatedFields(graph);
    
    graph.commit();
    return "Successfully Removed Deprecated Fields";
  }
  
  private void removeExtensionRelatedFields(OrientGraph graph)
  {
    removeExtensionFieldsFromKlassNodes(graph);
    removeExtensionFieldsFromRelationshipNodes(graph);
    removeExtensionFieldsFromKlassRelationshipNodes(graph);
  }
  
  private void removeExtensionFieldsFromKlassRelationshipNodes(OrientGraph graph)
  {
    Iterator<Vertex> klassRelationshipIterator = graph
        .getVertices(VertexLabelConstants.KLASS_RELATIONSHIP, new String[] {}, new String[] {}).iterator();
    
    while (klassRelationshipIterator.hasNext()) {
      Vertex klassRelationship = klassRelationshipIterator.next();
      Map<String, Object> relationshipSide = klassRelationship.getProperty(CommonConstants.RELATIONSHIP_SIDE);
      
      relationshipSide.remove("extensionAttributes");
      relationshipSide.remove("extensionTags");
      
      klassRelationship.setProperty(CommonConstants.RELATIONSHIP_SIDE, relationshipSide);
    }
    
  }
  
  private void removeExtensionFieldsFromRelationshipNodes(OrientGraph graph)
  {
    Iterator<Vertex> rootRelationshipIterator = graph.getVertices(VertexLabelConstants.ROOT_RELATIONSHIP, new String[] {}, new String[] {})
        .iterator();
    
    while (rootRelationshipIterator.hasNext()) {
      Vertex rootRelationship = rootRelationshipIterator.next();
      Map<String, Object> side1Map = rootRelationship.getProperty(CommonConstants.RELATIONSHIP_SIDE_1);
      Map<String, Object> side2Map = rootRelationship.getProperty(CommonConstants.RELATIONSHIP_SIDE_2);
      
      if(side1Map.get("code") == null) {
        side1Map.put("code", side1Map.get("id"));
      }
      side1Map.remove("extensionAttributes");
      side1Map.remove("extensionTags");
      rootRelationship.setProperty(CommonConstants.RELATIONSHIP_SIDE_1, side1Map);
      
      if(side2Map.get("code") == null) {
        side2Map.put("code", side2Map.get("id"));
      }
      
      side2Map.remove("extensionAttributes");
      side2Map.remove("extensionTags");
      rootRelationship.setProperty(CommonConstants.RELATIONSHIP_SIDE_2, side2Map);
    }
  }
  
  private void removeExtensionFieldsFromKlassNodes(OrientGraph graph)
  {
    Iterator<Vertex> klassIterator = graph.getVertices(VertexLabelConstants.ENTITY_TYPE_KLASS, new String[] {}, new String[] {}).iterator();
    
    while (klassIterator.hasNext()) {
      Vertex klassNode = klassIterator.next();
      List<Map<String, Object>> relationships = klassNode.getProperty(CommonConstants.RELATIONSHIPS);
      Map<String, Object> parent = klassNode.getProperty(CommonConstants.PARENT_PROPERTY);
      
      if (relationships != null && !relationships.isEmpty()) {
        removeExtensionFieldsFromRelationships(relationships);
        klassNode.setProperty(CommonConstants.RELATIONSHIPS, relationships);
      }
      
      if (parent != null) {
        removeExtensionFieldsFromParentRecursively(parent);
        klassNode.setProperty(CommonConstants.PARENT_PROPERTY, parent);
      }
      
    }
  }

  @SuppressWarnings("unchecked")
  private void removeExtensionFieldsFromRelationships(List<Map<String, Object>> relationships)
  {
    relationships.forEach(r -> {
      
      Map<String, Object> side1Map = (Map<String, Object>) r.get(CommonConstants.RELATIONSHIP_SIDE_1);
      Map<String, Object> side2Map = (Map<String, Object>) r.get(CommonConstants.RELATIONSHIP_SIDE_2);
      
      if (side1Map != null) {
        side1Map.remove("extensionAttributes");
        side1Map.remove("extensionTags");
      }
      
      if (side2Map != null) {
        side2Map.remove("extensionAttributes");
        side2Map.remove("extensionTags");
      }
      
      r.remove("extension");
    });
  }
  
  @SuppressWarnings("unchecked")
  private void removeExtensionFieldsFromParentRecursively(Map<String, Object> parent)
  {
    if (parent != null) {
      List<Map<String, Object>> relationships = (List<Map<String, Object>>) parent.get(CommonConstants.RELATIONSHIPS);
      
      if (relationships != null && !relationships.isEmpty()) {
        removeExtensionFieldsFromRelationships(relationships);
        removeExtensionFieldsFromParentRecursively((Map<String, Object>) parent.get(CommonConstants.PARENT_PROPERTY));
      }
    }
  }
  
  private void removeEventAndReferencesRelatedFields(OrientGraph graph)
  {
    Iterator<Vertex> rootKlassIterator = graph.getVertices(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS, new String[] {}, new String[] {})
        .iterator();
    while (rootKlassIterator.hasNext()) {
      Vertex klassNode = rootKlassIterator.next();
      
      if (klassNode.getProperty("events") != null) {
        klassNode.removeProperty("events");
      }
      if (klassNode.getProperty("references") != null) {
        klassNode.removeProperty("references");
      }
      
      Map<String, Object> parent = klassNode.getProperty(CommonConstants.PARENT_PROPERTY);
      
      if (parent != null) {
        recursivelyRemoveEventsAndReferencesFieldFromParent(parent);
        klassNode.setProperty(CommonConstants.PARENT_PROPERTY, parent);
      }
    }
  }
  
  @SuppressWarnings("unchecked")
  private void recursivelyRemoveEventsAndReferencesFieldFromParent(Map<String, Object> parent)
  {
    if (parent != null) {
      parent.remove("events");
      parent.remove("references");
      recursivelyRemoveEventsAndReferencesFieldFromParent((Map<String, Object>) parent.get(CommonConstants.PARENT_PROPERTY));
    }
  }
  
}
