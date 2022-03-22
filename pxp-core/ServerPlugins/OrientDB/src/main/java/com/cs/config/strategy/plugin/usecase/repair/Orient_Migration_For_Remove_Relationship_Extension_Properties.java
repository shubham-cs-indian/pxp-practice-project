package com.cs.config.strategy.plugin.usecase.repair;

import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class Orient_Migration_For_Remove_Relationship_Extension_Properties
    extends AbstractOrientMigration {
  
  public Orient_Migration_For_Remove_Relationship_Extension_Properties(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_For_Remove_Relationship_Extension_Properties/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    removeExtensionPropertiesFromRelationshipNodes(VertexLabelConstants.ROOT_RELATIONSHIP);
    removeExtensionPropertiesFromKlassNodes(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    
    UtilClass.getGraph()
        .commit();
    return null;
  }
  
  private void removeExtensionPropertiesFromRelationshipNodes(String vertexType)
  {
    Iterable<Vertex> relationshipNodes = getVerticesFromVertexType(vertexType);
    for (Vertex relationshipNode : relationshipNodes) {
      
      // Remove Extension (if present)
      if (relationshipNode.getProperty("extension") != null) {
        relationshipNode.removeProperty("extension");
      }
      // Remove Extension Attributes & Extension Tags
      Map<String, Object> side1 = relationshipNode.getProperty(IRelationship.SIDE1);
      Map<String, Object> side2 = relationshipNode.getProperty(IRelationship.SIDE2);
      removeExtensionProperties(side1);
      removeExtensionProperties(side2);
      relationshipNode.setProperty(IRelationship.SIDE1, side1);
      relationshipNode.setProperty(IRelationship.SIDE2, side2);
      
      // Fetch klass relationship nodes
      Iterable<Vertex> kRNodes = relationshipNode.getVertices(Direction.IN,
          RelationshipLabelConstants.HAS_PROPERTY);
      for (Vertex kRNode : kRNodes) {
        Map<String, Object> side = kRNode.getProperty(CommonConstants.RELATIONSHIP_SIDE);
        removeExtensionProperties(side);
        kRNode.setProperty(CommonConstants.RELATIONSHIP_SIDE, side);
      }
    }
  }
  
  private void removeExtensionPropertiesFromKlassNodes(String vertexType)
  {
    
    Iterable<Vertex> klassNodes = getVerticesFromVertexType(vertexType);
    for (Vertex klassNode : klassNodes) {
      List<Map<String, Object>> relationshipsList = klassNode
          .getProperty(IRelationshipSide.RELATIONSHIPS);
      if (relationshipsList != null) {        
        for (Map<String, Object> relationship : relationshipsList) {
          if (relationship.containsKey("extension")) {
            relationship.remove("extension");
          }
          removeExtensionPropertiesFromNatureRelationshipNode(relationship);
        }
        klassNode.setProperty(IRelationshipSide.RELATIONSHIPS, relationshipsList);
      }
    }
  }
  
  @SuppressWarnings("unchecked")
  private void removeExtensionPropertiesFromNatureRelationshipNode(
      Map<String, Object> relationshipMap)
  {
    Map<String, Object> side1 = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE1);
    Map<String, Object> side2 = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE2);
    removeExtensionProperties(side1);
    removeExtensionProperties(side2);
    relationshipMap.put(IRelationship.SIDE1, side1);
    relationshipMap.put(IRelationship.SIDE2, side2);
  }
  
  private void removeExtensionProperties(Map<String, Object> sideInfo)
  {
    if (sideInfo.containsKey("extensionAttributes")) {
      sideInfo.remove("extensionAttributes");
    }
    if (sideInfo.containsKey("extensionTags")) {
      sideInfo.remove("extensionTags");
    }
  }
  
  private Iterable<Vertex> getVerticesFromVertexType(String vertexType)
  {
    return UtilClass.getVerticesFromQuery("select * from " + vertexType);
  }
}
