package com.cs.config.strategy.plugin.usecase.base.taxonomy.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.attributiontaxonomy.util.AttributionTaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.exception.MultipleLinkFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public class DeleteTaxonomyUtil {
  
  private static List<String> getTaxonomyAndChildIds(List<String> taxonomyAndChildIds, String id)
      throws Exception
  {
    Vertex klassTaxonomy = null;
    try {
      klassTaxonomy = UtilClass.getVertexById(id, VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    }
    catch (NotFoundException e) {
      taxonomyAndChildIds.remove(id);
      return taxonomyAndChildIds;
    }
    
    Iterable<Vertex> childs = klassTaxonomy.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex child : childs) {
      String childId = child.getProperty(CommonConstants.CODE_PROPERTY);
      taxonomyAndChildIds.add(childId);
      taxonomyAndChildIds = getTaxonomyAndChildIds(taxonomyAndChildIds, childId);
    }
    
    return taxonomyAndChildIds;
  }
  
  private static void deleteKlassAndChildTaxonomies(String id, List<String> taxonomyAndChildIds,
      Set<Vertex> nodesToDelete, Set<Edge> edgesToDelete) throws Exception
  {
    Vertex klassTaxonomy = null;
    try {
      klassTaxonomy = UtilClass.getVertexById(id, VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    }
    catch (NotFoundException e) {
      return;
    }
    
    // delete filterable tag vertex
    Iterable<Vertex> filterablTags = klassTaxonomy.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_FILTERABLE_TAG);
    for (Vertex filterablTag : filterablTags) {
      filterablTag.remove();
    }
    
    // delete sortable attribute vertex
    Iterable<Vertex> sortableAttributes = klassTaxonomy.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_SORTABLE_ATTRIBUTE);
    for (Vertex sortableAttribute : sortableAttributes) {
      sortableAttribute.remove();
    }
    
    // delete default filter vertex
    Iterable<Vertex> defaultFilters = klassTaxonomy.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_DEFAULT_FILTER);
    for (Vertex defaultFilter : defaultFilters) {
      defaultFilter.remove();
    }
    
    Iterable<Edge> sectionOfRelationships = klassTaxonomy.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
    
    for (Edge sectionRelation : sectionOfRelationships) {
      
      Vertex sectionNode = sectionRelation.getVertex(Direction.OUT);
      boolean isKlassDeleted = true;
      KlassUtils.deleteSectionFromKlass(sectionRelation, sectionNode, taxonomyAndChildIds, id,
          nodesToDelete, edgesToDelete, isKlassDeleted, VertexLabelConstants.ROOT_KLASS_TAXONOMY,
          new HashMap<>());
    }
    
    // delete taxonomy permissions..
    Iterable<Edge> hasGlobalPermissionRelationships = klassTaxonomy.getEdges(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION);
    for (Edge globalPermissionRelationship : hasGlobalPermissionRelationships) {
      Vertex globalPermissionNode = globalPermissionRelationship.getVertex(Direction.IN);
      nodesToDelete.add(globalPermissionNode);
    }
    
    // deleteKlass property nodes associated with taxonomy
    Iterable<Vertex> klassTaxonomyProperties = klassTaxonomy.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_PROPERTY);
    for (Vertex klassTaxonomyProperty : klassTaxonomyProperties) {
      nodesToDelete.add(klassTaxonomyProperty);
    }
    
    /*Boolean isAttributionTaxonomy = klassTaxonomy.getProperty(IMasterTaxonomy.IS_ATTRIBUTION_TAXONOMY);
    Boolean isLanguageTaxonomy = klassTaxonomy.getProperty(ILanguageTaxonomy.IS_LANGUAGE_TAXONOMY);*/
    Boolean isAttributionTaxonomy = klassTaxonomy
        .getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY)
        .equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY);
    Boolean isLanguageTaxonomy = klassTaxonomy.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY)
        .equals(VertexLabelConstants.LANGUAGE);
    isAttributionTaxonomy = isAttributionTaxonomy == null ? false : isAttributionTaxonomy;
    isLanguageTaxonomy = isLanguageTaxonomy == null ? false : isLanguageTaxonomy;
    
    Boolean isTag = klassTaxonomy.getProperty(IMasterTaxonomy.IS_TAG);
    isTag = isTag == null ? false : isTag;
    if (isAttributionTaxonomy) {
      removeAllLinkedNodeLink(nodesToDelete, edgesToDelete, klassTaxonomy,
          VertexLabelConstants.ATTRIBUTION_TAXONOMY,
          VertexLabelConstants.ATTRIBUTION_TAXONOMY_LEVEL);
    }
    else if (isLanguageTaxonomy) {
      removeAllLinkedNodeLink(nodesToDelete, edgesToDelete, klassTaxonomy,
          VertexLabelConstants.LANGUAGE, VertexLabelConstants.LANGUAGE_TAXONOMY_LEVEL);
    }
    if ((isLanguageTaxonomy || isAttributionTaxonomy) && isTag) {
      klassTaxonomy.setProperty(IMasterTaxonomy.IS_TAXONOMY, false);
      klassTaxonomy.setProperty(IMasterTaxonomy.TAG_LEVEL_SEQUENCE, new ArrayList<>());
      klassTaxonomy.setProperty(IMasterTaxonomy.TAXONOMY_TYPE, "");
      return;
    }
    
    GlobalPermissionUtils.deleteAllPermissionNodesForKlass(klassTaxonomy);
    List<String> klassTaxonomyIds = new ArrayList<>();
    klassTaxonomyIds.add(klassTaxonomy.getId()
        .toString());
    // delete relationship associated with taxonomy
    deleteRelationshipsForKlassTaxonomies(klassTaxonomyIds);
    nodesToDelete.add(klassTaxonomy); // delete klass taxonomy vertex
  }
  
  private static void removeAllLinkedNodeLink(Set<Vertex> nodesToDelete, Set<Edge> edgesToDelete,
      Vertex klassTaxonomy, String taxonomyVetexLabel, String taxonomyVetextLevelLabel)
      throws Exception, MultipleLinkFoundException
  {
    List<String> tagLevelsSequence = klassTaxonomy.getProperty(IMasterTaxonomy.TAG_LEVEL_SEQUENCE);
    tagLevelsSequence = tagLevelsSequence == null ? new ArrayList<>() : tagLevelsSequence;
    for (String tagLevelSequence : tagLevelsSequence) {
      Vertex levelNode = UtilClass.getVertexById(tagLevelSequence, taxonomyVetextLevelLabel);
      nodesToDelete.add(levelNode);
    }
    
    // remove edges between attribution taxonomies
    Iterable<Edge> childOfEdges = klassTaxonomy.getEdges(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    Integer count = 0;
    for (Edge childOfEdge : childOfEdges) {
      if (count > 1) {
        throw new MultipleLinkFoundException();
      }
      Vertex parent = childOfEdge.getVertex(Direction.IN);
      String vertexType = ((OrientVertex) parent).getType()
          .toString();
      if (vertexType.equals(taxonomyVetexLabel)) {
        edgesToDelete.add(childOfEdge);
      }
    }
    
    removeTasks(klassTaxonomy);
    removeEmbeddedVariants(klassTaxonomy);
    removeDataRules(klassTaxonomy);
  }
  
  private static void removeDataRules(Vertex klassTaxonomy)
  {
    Iterable<Edge> edges = klassTaxonomy.getEdges(Direction.OUT,
        RelationshipLabelConstants.DATA_RULES);
    for (Edge edge : edges) {
      edge.remove();
    }
  }
  
  private static void removeEmbeddedVariants(Vertex klassTaxonomy)
  {
    Iterable<Edge> edges = klassTaxonomy.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_KLASS);
    for (Edge edge : edges) {
      edge.remove();
    }
  }
  
  private static void removeTasks(Vertex klassTaxonomy)
  {
    Iterable<Edge> edges = klassTaxonomy.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_TASK);
    for (Edge edge : edges) {
      edge.remove();
    }
  }
  
  /**
   * delete relationships for klass taxonomies
   *
   * @author Janak.Gurme
   * @param klassTaxonomyIds
   * @throws Exception
   */
  public static void deleteRelationshipsForKlassTaxonomies(List<String> klassTaxonomyIds)
      throws Exception
  {
    List<String> relationshipIdsToDelete = RelationshipUtils
        .getNonInheritedRelationshipIdsForTypes(klassTaxonomyIds);
    RelationshipUtils.deleteRelationships(relationshipIdsToDelete, new ArrayList<>());
  }
  
  public static void deleteKlassTaxonomies(String id, String taxonomyVertexType,  List<Map<String , Object>> auditInfoList) throws Exception
  {
    Vertex klassTaxonomy = null;
    Elements element = null;
    try {
      klassTaxonomy = UtilClass.getVertexById(id, taxonomyVertexType);
      element = taxonomyVertexType.equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY)
              ? Elements.MASTER_TAXONOMY_CONFIGURATION_TITLE
              : Elements.HIERARCHIES_TITLE;
      AuditLogUtils.fillAuditLoginfo(auditInfoList, klassTaxonomy, Entities.TAXONOMIES, element);
    }
    catch (NotFoundException e) {
      return;
    }
    Vertex parentTaxonomy = AttributionTaxonomyUtil.getParentTaxonomy(klassTaxonomy);
    if (parentTaxonomy != null) {
      Integer childCount = parentTaxonomy.getProperty(ITaxonomy.CHILD_COUNT);
      parentTaxonomy.setProperty(ITaxonomy.CHILD_COUNT, --childCount);
    }
    
    List<String> taxonomyAndChildIds = new ArrayList<>();
    taxonomyAndChildIds.add(id);
    taxonomyAndChildIds = getTaxonomyAndChildIds(taxonomyAndChildIds, id);
    Set<Vertex> nodesToDelete = new HashSet<Vertex>();
    Set<Edge> edgesToDelete = new HashSet<Edge>();
    for (String taxonomyId : taxonomyAndChildIds) {
      deleteKlassAndChildTaxonomies(taxonomyId, taxonomyAndChildIds, nodesToDelete, edgesToDelete);
    }
    for (Edge edge : edgesToDelete) {
      edge.remove();
    }
    for (Vertex node : nodesToDelete) {
      node.remove();
    }
  }
  /*
  private static void deleteKlassTaxonomyTemplate(Vertex taxonomyNode)
  {
    Iterator<Edge> edgeIterator = taxonomyNode
        .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_TEMPLATE)
        .iterator();
    if (edgeIterator.hasNext()) {
      Edge hasTemplateEdge = edgeIterator.next();
      Boolean isInherited = hasTemplateEdge.getProperty(CommonConstants.IS_INHERITED_PROPERTY);
      if(isInherited == null || !isInherited) {
        Vertex templateNode = hasTemplateEdge.getVertex(Direction.IN);
        DeleteTemplateUtils.deleteTemplateNode(templateNode);
      }
    }
  }*/
  
}
