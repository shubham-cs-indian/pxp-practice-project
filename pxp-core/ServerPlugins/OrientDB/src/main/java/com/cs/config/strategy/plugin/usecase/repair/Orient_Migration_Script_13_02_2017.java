/*
 * (product types branch merge into master : so we need nature type in klass and
 * context type in contexts)
 *
 */

package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.datarule.util.DataRuleUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Orient_Migration_Script_13_02_2017 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_13_02_2017(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = null;
    
    // For contexts
    Iterator<Vertex> iterator = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.VARIANT_CONTEXT)
        .iterator();
    while (iterator.hasNext()) {
      Vertex context = iterator.next();
      context.removeProperty("editableProperties");
      context.setProperty("type", "contextualVariant");
    }
    
    // For Klasses
    iterator = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_KLASS)
        .iterator();
    while (iterator.hasNext()) {
      Vertex klass = iterator.next();
      klass.setProperty("isNature", true);
      klass.setProperty("natureType", "singleArticle");
      // klass.setProperty("previewImage", null);
    }
    deleteAttr(UtilClass.getGraph(), "multiclassificationattribute");
    deleteAttr(UtilClass.getGraph(), "taxonomyattribute");
    deleteAttr(UtilClass.getGraph(), "typeattribute");
    
    // For Klasses
    iterator = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS)
        .iterator();
    while (iterator.hasNext()) {
      Vertex klass = iterator.next();
      klass.removeProperty("variantContexts");
      klass.removeProperty("parent");
      // klass.setProperty("previewImage", null);
    }
    
    UtilClass.getGraph()
        .commit();
    return returnMap;
  }
  
  private void deleteAttr(OrientGraph graph, String id) throws Exception
  {
    Vertex attributeNode = UtilClass.getVertexByIndexedId(id,
        VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
    Iterator<Edge> iterator = attributeNode
        .getEdges(com.tinkerpop.blueprints.Direction.IN,
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
    
    deleteSectionElementNodesAttached(attributeNode);
    deletePermissionNodesAttached(attributeNode);
    deleteTaxonomyNodesAttached(attributeNode);
    AttributeUtils.deleteOperatorNodesAttached(attributeNode);
    AttributeUtils.deleteConcatenatedNodesAttached(attributeNode);
    
    Iterable<Edge> attributeLinkRelationships = attributeNode.getEdges(
        com.tinkerpop.blueprints.Direction.IN, RelationshipLabelConstants.STRUCTURE_ATTRIBUTE_LINK);
    
    // iterate over all attribute link relationships
    for (Edge attributeLinkRelation : attributeLinkRelationships) {
      Vertex structureNode = attributeLinkRelation.getVertex(Direction.OUT);
      deleteStructureNode(structureNode);
    }
    
    DataRuleUtils.deleteVerticesWithInDirection(attributeNode,
        RelationshipLabelConstants.ENTITY_RULE_VIOLATION_LINK);
    DataRuleUtils.deleteIntermediateVerticesWithInDirection(attributeNode,
        RelationshipLabelConstants.ATTRIBUTE_DATA_RULE_LINK);
    DataRuleUtils.deleteVerticesWithInDirection(attributeNode,
        RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK);
    DataRuleUtils.deleteRuleNodesLinkedToEntityNode(attributeNode,
        RelationshipLabelConstants.HAS_ATTRIBUTE_LINK);
    attributeNode.remove();
    // }
  }
  
  public void deleteSectionElementNodesAttached(Vertex attributeNode)
  {
    Iterator<Edge> iterator = attributeNode
        .getEdges(Direction.IN, RelationshipLabelConstants.HAS_PROPERTY)
        .iterator();
    while (iterator.hasNext()) {
      Edge hasProperty = iterator.next();
      Vertex klassPropertyNode = hasProperty.getVertex(Direction.OUT);
      KlassUtils.removeLinkbetweenSectionElementAndNotificationSetting(klassPropertyNode);
      // KlassUtils.removeLinkbetweenSectionElementAndTagGroup(klassPropertyNode);
      // KlassUtils.removeAttachedSectionElementPermissionNodes(klassPropertyNode);
      klassPropertyNode.remove();
    }
  }
  
  public void deleteTaxonomyNodesAttached(Vertex attributeNode)
  {
    Iterable<Vertex> vertices = attributeNode.getVertices(Direction.IN,
        RelationshipLabelConstants.ATTRIBUTE_SORTABLE_ATTRIBUTE_LINK);
    
    for (Vertex vertex : vertices) {
      vertex.remove(); // delete the intermediate node linked to attribute
    }
  }
  
  private void deletePermissionNodesAttached(Vertex attributeNode)
  {
    Iterable<Vertex> iterable = attributeNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION);
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
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_13_02_2017/*" };
  }
}
