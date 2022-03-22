package com.cs.config.strategy.plugin.usecase.klass.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.IteratorUtils;

import com.cs.config.strategy.plugin.usecase.datarule.util.DataRuleUtils;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class DeleteKlassUtil {
  
  @SuppressWarnings("unchecked")
  public static void deleteKlassNode(Vertex klassNode, List<String> selfAndChildKlassIds,
      Set<Vertex> nodesToDelete, Set<Edge> relationshipsToDelete, String vertexLabel)
      throws Exception
  {
    String klassId = (String) klassNode.getProperty(CommonConstants.CODE_PROPERTY);
    
    Iterable<Edge> sectionOfRelationships = klassNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
    
    for (Edge sectionRelation : sectionOfRelationships) {
      
      Vertex sectionNode = sectionRelation.getVertex(Direction.OUT);
      boolean isKlassDeleted = true;
      KlassUtils.deleteSectionFromKlass(sectionRelation, sectionNode, selfAndChildKlassIds, klassId,
          nodesToDelete, relationshipsToDelete, isKlassDeleted, vertexLabel, new HashMap<>());
    }
    
    Iterable<Edge> hasGlobalPermissionRelationships = klassNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION);
    for (Edge globalPermissionRelationship : hasGlobalPermissionRelationships) {
      Vertex globalPermissionNode = globalPermissionRelationship.getVertex(Direction.IN);
      Iterator<Edge> iterator = globalPermissionNode
          .getEdges(Direction.IN, RelationshipLabelConstants.HAS_KLASS_TAXONOMY_GLOBAL_PERMISSIONS)
          .iterator();
      Edge globalPermissionForRelationship = null;
      while (iterator.hasNext()) {
        globalPermissionForRelationship = iterator.next();
      }
      
      relationshipsToDelete.add(globalPermissionForRelationship);
      relationshipsToDelete.add(globalPermissionRelationship);
      nodesToDelete.add(globalPermissionNode);
    }
    
    // taxonomy node deletion
    Iterator<Edge> iterator = klassNode
        .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_TAXONOMY_SETTING_OF)
        .iterator();
    Edge taxonomySettingOf = null;
    while (iterator.hasNext()) {
      taxonomySettingOf = iterator.next();
      relationshipsToDelete.add(taxonomySettingOf);
    }
    
    KlassUtils.deleteAllowedTypeRelationships(klassNode);
    DataRuleUtils.deleteIntermediateVerticesWithInDirection(klassNode,
        RelationshipLabelConstants.KLASS_DATA_RULE_LINK);
    DataRuleUtils.deleteVerticesWithInDirection(klassNode,
        RelationshipLabelConstants.ENTITY_RULE_VIOLATION_LINK);
    
    GlobalPermissionUtils.deleteAllPermissionNodesForKlass(klassNode);
    
    // delete contextnodes and contextTagNodes on deletion of embedded and
    // gtin classes
    String natureType = klassNode.getProperty(IKlass.NATURE_TYPE);
    if (natureType != null) {
      if (natureType.equals(CommonConstants.GTIN_KLASS_TYPE)
          || natureType.equals(CommonConstants.EMBEDDED_KLASS_TYPE)
          || natureType.equals(CommonConstants.TECHNICAL_IMAGE_VARIANT_KLASS_TYPE)
          || natureType.equals(CommonConstants.LANGUAGE_KLASS_TYPE)) {
        deleteContextNodesAndContextTags(klassNode, nodesToDelete);
      }
      else {
        Iterable<Vertex> intermediateNode = klassNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.KLASS_CONTEXTUAL_PROPAGABLE_PROPERTIES_LINK);
        nodesToDelete.addAll((IteratorUtils.toList(intermediateNode.iterator())));
      }
    }
    nodesToDelete.add(klassNode);
  }
  
  @SuppressWarnings("unchecked")
  private static void deleteContextNodesAndContextTags(Vertex klassNode, Set<Vertex> nodesToDelete)
      throws Exception
  {
    Iterator<Vertex> contextNodes = klassNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
        .iterator();
    Vertex contextNode = null;
    List<Vertex> vertexToRemove = new ArrayList<>();
    while (contextNodes.hasNext()) {
      contextNode = contextNodes.next();
      Iterable<Vertex> contextTags = contextNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_CONTEXT_TAG);
      for (Vertex contextTag : contextTags) {
        contextTag.remove();
      }
      TabUtils.updateTabOnEntityDelete(contextNode);
      vertexToRemove.add(contextNode);
    }
    Iterable<Vertex> intermediateNode = klassNode.getVertices(Direction.IN,
        RelationshipLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES_KLASS_LINK);
    vertexToRemove.addAll(IteratorUtils.toList(intermediateNode.iterator()));
    
    nodesToDelete.addAll(vertexToRemove);
  }
  
  /*
  private static void deleteContextTemplate(Vertex contextNode)
  {
    Iterator<Vertex> templateIterator = contextNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TEMPLATE)
        .iterator();
    if (templateIterator.hasNext()) {
      Vertex templateNode = templateIterator.next();
      DeleteTemplateUtils.deleteTemplateNode(templateNode);
    }
  }
  */
  
  /*public static void deleteKlassTemplate(Vertex klassNode)
  {
    Iterator<Edge> edgeIterator = klassNode.getEdges(Direction.OUT, RelationshipLabelConstants.HAS_TEMPLATE).iterator();
    if(edgeIterator.hasNext()){
      Edge hasTemplateEdge = edgeIterator.next();
      Boolean isInherited = hasTemplateEdge.getProperty(CommonConstants.IS_INHERITED_PROPERTY);
      if(!isInherited){
        Vertex templateNode = hasTemplateEdge.getVertex(Direction.IN);
        DeleteTemplateUtils.deleteTemplateNode(templateNode);
      }
    }
  }*/
  
  public static void deleteNestedHierarchyWithConnectedNodes(List<String> idsToDelete,
      List<String> deletedIds, Set<Vertex> nodesToDelete, Set<Edge> relationshipsToDelete,
      String vertexLabel, Set<Vertex> klassNodesToDelete) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    for (String id : idsToDelete) {
      
      Vertex klassNode = null;
      try {
        klassNode = UtilClass.getVertexById(id, vertexLabel);
        if(ValidationUtils.vaildateIfStandardEntity(klassNode))
          continue;
        klassNodesToDelete.add(klassNode);
      }
      catch (NotFoundException e) {
        continue;
      }
      
      Iterator<Edge> iterator = klassNode
          .getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
          .iterator();
      
      while (iterator.hasNext()) {
        relationshipsToDelete.add(iterator.next());
      }
      
      String rid = (String) klassNode.getId()
          .toString();
      Iterable<Vertex> iterable = graph
          .command(new OCommandSQL(
              "select from(traverse in('Child_Of') from " + rid + "  strategy BREADTH_FIRST)"))
          .execute();
      
      for (Vertex childKlassNode : iterable) {
        List<String> selfAndChildKlassIds = new ArrayList<>();
        String childKlassIdstring = (String) childKlassNode
            .getProperty(CommonConstants.CODE_PROPERTY);
        deletedIds.add(childKlassIdstring);
        Iterator<Edge> i = childKlassNode
            .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
            .iterator();
        selfAndChildKlassIds.add(childKlassIdstring);
        
        while (i.hasNext()) {
          Edge childOfRelations = i.next();
          relationshipsToDelete.add(childOfRelations);
          selfAndChildKlassIds.add(childKlassIdstring);
          DeleteKlassUtil.deleteKlassNode(childKlassNode, selfAndChildKlassIds, nodesToDelete,
              relationshipsToDelete, vertexLabel);
        }
        DeleteKlassUtil.deleteKlassNode(childKlassNode, selfAndChildKlassIds, nodesToDelete,
            relationshipsToDelete, vertexLabel);
      }
    }
  }
}
