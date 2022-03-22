package com.cs.config.strategy.plugin.usecase.relationship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class DeleteRelationship extends AbstractOrientPlugin {
  
  public DeleteRelationship(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> relationshipIds = (List<String>) requestMap.get("ids");
    
    List<Map<String, Object>> auditInfoList = new ArrayList<>();
    
    List<String> deletedIds = RelationshipUtils.deleteRelationships(relationshipIds, auditInfoList);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("success", deletedIds);
    responseMap.put(IBulkDeleteReturnModel.AUDIT_LOG_INFO, auditInfoList);
    UtilClass.getGraph()
        .commit();
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteRelationship/*" };
  }
  
  /*private List<String> deleteRelationships(List<String> relationshipIds)
      throws Exception
  {
  
    OrientBaseGraph graph = UtilClass.getGraph();
    List<String> deletedIds = new ArrayList<>();
  
    Vertex relationshipNode = null;
    for (String id : relationshipIds) {
      Iterator<Vertex> resultIterator = graph
          .getVertices(VertexLabelConstants.ROOT_RELATIONSHIP,
              new String[] { CommonConstants.CODE_PROPERTY }, new Object[] { id })
          .iterator();
      // relationshipNode
      if (resultIterator.hasNext()) {
        relationshipNode = resultIterator.next();
        Iterator<Edge> iterator = relationshipNode.getEdges(com.tinkerpop.blueprints.Direction.IN,
            RelationshipLabelConstants.RELATIONSHIPLABEL_VALIDATOR_OF)
            .iterator();
        Edge validatorOfRelationship = null;
        while (iterator.hasNext()) {
          validatorOfRelationship = iterator.next();
  
          if (validatorOfRelationship != null) {
            Vertex validatorNode = validatorOfRelationship
                .getVertex(com.tinkerpop.blueprints.Direction.OUT);
            validatorOfRelationship.remove();
            validatorNode.remove();
          }
        }
  
        deleteSectionElementNodesAttached(relationshipNode);
        deletePermissionNodesAttached(relationshipNode);
  
        Iterable<Edge> relationshipLinkRelationships = relationshipNode.getEdges(
            com.tinkerpop.blueprints.Direction.IN,
            RelationshipLabelConstants.STRUCTURE_RELATIONSHIP_LINK);
  
        // iterate over all relationship link relationships
        for (Edge relationshipLinkRelation : relationshipLinkRelationships) {
          Vertex structureNode = relationshipLinkRelation.getVertex(Direction.OUT);
          deleteStructureNode(structureNode);
          // relationshipLinkRelation.remove();
        }
        DataRuleUtils.deleteIntermediateVerticesWithInDirection(relationshipNode, RelationshipLabelConstants.RELATIONSHIP_DATA_RULE_LINK);
        DataRuleUtils.deleteVerticesWithInDirection(relationshipNode, RelationshipLabelConstants.ENTITY_RULE_VIOLATION_LINK);
        relationshipNode.remove();
      }
      deletedIds.add(id);
    }
    return deletedIds;
  }
  
  private void deletePermissionNodesAttached(Vertex attributeNode)
  {
    Iterable<Vertex> iterable = attributeNode.getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION);
    for (Vertex permissionNode : iterable) {
      permissionNode.remove();
    }
  }
  
  private void deleteStructureNode(Vertex deletedStructureNode)
  {
    // TODO Auto-generated method stub
    Set<Vertex> nodesToDelete = new HashSet<>();
    Set<Edge> relationshipsToDelete = new HashSet<>();
    Set<Vertex> positionNodes = new HashSet<>();
  
    // get all position nodes...
    Iterable<Edge> structureChildAtRelationships = deletedStructureNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.STRUCTURE_CHILD_AT);
    for (Edge structureChildAtRelation : structureChildAtRelationships) {
      Vertex positionNode = structureChildAtRelation.getVertex(Direction.IN);
      positionNodes.add(positionNode);
      relationshipsToDelete.add(structureChildAtRelation);
    }
  
    Iterator<Edge> iterator = deletedStructureNode
        .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_VALIDATOR_OF)
        .iterator();
    Edge validatorOfRelation = null;
    while (iterator.hasNext()) {
      validatorOfRelation = iterator.next();
    }
    if (validatorOfRelation != null) {
      Vertex validatorNode = validatorOfRelation.getVertex(Direction.OUT);
      relationshipsToDelete.add(validatorOfRelation);
      nodesToDelete.add(validatorNode);
    }
  
    Iterable<Edge> viewSettingsOfRelationships = deletedStructureNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_SETTING_OF);
  
    for (Edge viewSettingRelation : viewSettingsOfRelationships) {
      Vertex viewSettingNode = viewSettingRelation.getVertex(Direction.OUT);
  
      Iterator<Edge> edgeIterator = viewSettingNode
          .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_OF)
          .iterator();
      Edge roleOfRelationship = null;
      while (edgeIterator.hasNext()) {
        roleOfRelationship = iterator.next();
        relationshipsToDelete.add(roleOfRelationship);
      }
  
      edgeIterator = viewSettingNode
          .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_TREE_SETTING_OF)
          .iterator();
      Edge treeSettingOfRelationship = null;
      while (edgeIterator.hasNext()) {
        treeSettingOfRelationship = iterator.next();
        Vertex treeSettingNode = treeSettingOfRelationship.getVertex(Direction.OUT);
        relationshipsToDelete.add(treeSettingOfRelationship);
        nodesToDelete.add(treeSettingNode);
      }
      edgeIterator = viewSettingNode
          .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_EDITOR_SETTING_OF)
          .iterator();
      Edge editorSettingOfRelationship = null;
      while (edgeIterator.hasNext()) {
        editorSettingOfRelationship = edgeIterator.next();
        Vertex editorSettingNode = editorSettingOfRelationship.getVertex(Direction.OUT);
        relationshipsToDelete.add(editorSettingOfRelationship);
        nodesToDelete.add(editorSettingNode);
      }
  
      relationshipsToDelete.add(viewSettingRelation);
      nodesToDelete.add(viewSettingNode);
    }
  
    Iterable<Edge> roleViewSettingsOfRelationships = deletedStructureNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_VIEW_SETTING_OF);
  
    for (Edge roleViewSettingRelation : roleViewSettingsOfRelationships) {
      Vertex roleViewSettingNode = roleViewSettingRelation.getVertex(Direction.OUT);
      Iterator<Edge> edgeIterator = roleViewSettingNode
          .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_VIEW_OF)
          .iterator();
  
      Edge viewOfRelationship = null;
      while (edgeIterator.hasNext()) {
        viewOfRelationship = edgeIterator.next();
        relationshipsToDelete.add(viewOfRelationship);
      }
  
      edgeIterator = roleViewSettingNode
          .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_OF)
          .iterator();
      Edge roleOfRelationship = null;
      while (edgeIterator.hasNext()) {
        roleOfRelationship = edgeIterator.next();
        relationshipsToDelete.add(roleOfRelationship);
      }
      relationshipsToDelete.add(roleViewSettingRelation);
      nodesToDelete.add(roleViewSettingNode);
    }
  
    nodesToDelete.add(deletedStructureNode);
  
    for (Vertex positionNode : positionNodes) {
      Iterable<Edge> structureChildrenRelationships = positionNode.getEdges(Direction.IN,
          RelationshipLabelConstants.STRUCTURE_CHILD_AT);
  
      boolean isEmptyPosition = true;
      for (Edge relationship : structureChildrenRelationships) {
        isEmptyPosition = false;
        break;
      }
  
      if (isEmptyPosition) {
        Iterable<Edge> structurePositionOfRelationships = positionNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.KLASS_STRUCTURE_CHILD_POSITION_OF);
  
        for (Edge structureChildPositionRelationship : structurePositionOfRelationships) {
          relationshipsToDelete.add(structureChildPositionRelationship);
        }
        nodesToDelete.add(positionNode);
      }
    }
  
    for (Edge relationship : relationshipsToDelete) {
      if (relationship != null) {
        relationship.remove();
      }
    }
    for (Vertex node : nodesToDelete) {
      node.remove();
    }
  
  }
  
  public void deleteSectionElementNodesAttachedOld(Vertex relationshipNode, OrientGraph graph)
  {
  
    Iterator<Edge> iterator = relationshipNode
        .getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO)
        .iterator();
  
    while (iterator.hasNext()) {
      Edge entityTo = iterator.next();
      Vertex sectionElementNode = entityTo.getVertex(Direction.IN);
  
      KlassUtils.removeLinkbetweenSectionElementAndNotificationSetting(sectionElementNode);
  
      // Remove Relationship Section
      Iterator<Edge> elementIterator = sectionElementNode
          .getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_ELEMENT_OF)
          .iterator();
      while (elementIterator.hasNext()) {
        Edge entityTo1 = elementIterator.next();
        Vertex sectionElementPosition = entityTo1.getVertex(Direction.IN);
        Iterator<Edge> positionIterator = sectionElementPosition
            .getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_POSITION_OF)
            .iterator();
        while (positionIterator.hasNext()) {
          Edge entityTo2 = positionIterator.next();
          Vertex sectionToRemove = entityTo2.getVertex(Direction.IN);
          deleteLinkedRelationships(graph, sectionToRemove.getProperty(CommonConstants.CODE_PROPERTY)
              .toString());
          sectionToRemove.remove();
        }
      }
      // remove attached permission nodes..
      Iterable<Edge> sectionPermissionForRelationships = sectionElementNode.getEdges(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_PERMISSION_FOR);
      for (Edge sectionPermissionRelationship : sectionPermissionForRelationships) {
        Vertex permissionNode = sectionPermissionRelationship.getVertex(Direction.OUT);
        Edge roleOfRelation = permissionNode
            .getEdges(Direction.IN, RelationshipConstants.RELATIONSHIPLABEL_ROLE_OF).iterator()
            .next();
        // relationshipsToDelete.add(roleOfRelation);
        // relationshipsToDelete.add(sectionPermissionRelationship);
        // System.out.println("v = " + permissionNode);
        permissionNode.remove();
      }
  
      Iterable<Edge> allSectionElementRelationships = sectionElementNode.getEdges(Direction.BOTH);
  
      for (Edge sectionElementRelationship : allSectionElementRelationships) {
        sectionElementRelationship.remove();
      }
  
      sectionElementNode.remove();
    }
  
  }
  
  
  private void deleteSectionElementNodesAttached(Vertex relationshipNode) throws Exception
  {
  
    Iterator<Vertex> iterator = relationshipNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO)
        .iterator();
  
    while (iterator.hasNext()) {
  
      Vertex propertyCollectionNode = iterator.next();
      //KlassUtils.removeLinkbetweenSectionElementAndNotificationSetting(propertyCollectionNode);
  
      // Remove Relationship Section
      Iterable<Vertex> elementIterator = propertyCollectionNode
          .getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO);
      int size = 0;
      for(Vertex value : elementIterator) {
         size++;
      }
      if (size == 1) {
        Iterator<Vertex> sectionsIterator = propertyCollectionNode
            .getVertices(Direction.OUT, RelationshipLabelConstants.PROPERTY_COLLECTION_OF)
            .iterator();
        while (sectionsIterator.hasNext()) {
          Vertex sectionToRemove = sectionsIterator.next();
          deleteLinkedRelationships(graph, sectionToRemove.getProperty(CommonConstants.CODE_PROPERTY)
              .toString());
          Iterable<Edge> previousSections = sectionToRemove.getEdges(Direction.OUT,
              RelationshipLabelConstants.PREVIOUS_SECTION);
          Set<Edge> edgesToDelete = new HashSet<>();
          for (Edge previousSection : previousSections) {
            KlassUtils.updateSequenceOfSectionNodes(sectionToRemove,
                previousSection.getProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY),
                previousSection.getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY), edgesToDelete);
          }
          for (Edge edge : edgesToDelete) {
            edge.remove();
          }
          sectionToRemove.remove();
        }
  
        deletePropertyCollectionPermissionNodesAttached(propertyCollectionNode);
        propertyCollectionNode.remove();
      }
    }
  
  Iterator<Vertex> iterator = relationshipNode
      .getVertices(Direction.IN, RelationshipLabelConstants.HAS_PROPERTY)
        .iterator();
  
    while (iterator.hasNext()) {
      Vertex klassPropertyNode = iterator.next();
      klassPropertyNode.remove();
    }
  
  }
  
  private void deletePropertyCollectionPermissionNodesAttached(Vertex propertyCollectionNode)
  {
    Iterable<Vertex> iterable = propertyCollectionNode.getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION);
    for (Vertex permissionNode : iterable) {
      permissionNode.remove();
    }
  }
  
  public void deleteLinkedRelationships(OrientGraph graph, String sectionId)
  {
    Iterable<Vertex> iterable = graph.command(
        new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP_INSTANCE))
        .execute();
    for (Vertex relationshipNode : iterable) {
      if (relationshipNode.getProperty("relationshipMappingId")
          .toString()
          .equals(sectionId)) {
        Iterable<Edge> relationEdges = relationshipNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.RELATIONSHIPLABEL_RELATION_TO);
        for (Edge relationEdge : relationEdges) {
          relationEdge.remove();
        }
        relationshipNode.remove();
      }
    }
  }
  */
}
