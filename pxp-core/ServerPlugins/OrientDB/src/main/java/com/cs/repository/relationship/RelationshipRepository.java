package com.cs.repository.relationship;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.runtime.interactor.constants.application.ProcessConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class RelationshipRepository {
  
  public static Iterable<Vertex> getRelationshipNodesForRelationshipInheritance(
      Map<String, Object> requestMap)
  {
    Long from = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.SIZE)
        .toString());
    String sortBy = (String) requestMap.get(IConfigGetAllRequestModel.SORT_BY);
    String sortOrder = (String) requestMap.get(IConfigGetAllRequestModel.SORT_ORDER);
    String searchColumn = (String) requestMap.get(IConfigGetAllRequestModel.SEARCH_COLUMN);
    String searchText = (String) requestMap.get(IConfigGetAllRequestModel.SEARCH_TEXT);
    
    StringBuilder cardinalityQuery = new StringBuilder(
        "(side1.cardinality = '" + CommonConstants.CARDINALITY_MANY + "' or side2.cardinality = '"
            + CommonConstants.CARDINALITY_MANY + "')");
    StringBuilder searchTextQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(cardinalityQuery, searchTextQuery);
    
    String query = "select from Relationship " + conditionQuery + " order by "
        + EntityUtil.getLanguageConvertedField(sortBy) + " " + sortOrder + " skip " + from
        + " limit " + size;
    return UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
  }
  
  public static Long getRelationshipNodesCountForRelationshipInheritance(
      Map<String, Object> requestMap)
  {
    String searchColumn = (String) requestMap.get(IConfigGetAllRequestModel.SEARCH_COLUMN);
    String searchText = (String) requestMap.get(IConfigGetAllRequestModel.SEARCH_TEXT);
    
    StringBuilder cardinalityQuery = new StringBuilder(
        "(side1.cardinality = '" + CommonConstants.CARDINALITY_MANY + "' or side2.cardinality = '"
            + CommonConstants.CARDINALITY_MANY + "')");
    StringBuilder searchTextQuery = new StringBuilder(
        EntityUtil.getLanguageConvertedField(searchColumn) + " like '%" + searchText + "%'");
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(cardinalityQuery, searchTextQuery);
    String query = "select count(*) from Relationship " + conditionQuery;
    return EntityUtil.executeCountQueryToGetTotalCount(query);
  }
  
  public static Edge getRelationshipInheritanceEdgeBetweenKRAndRelationshipVertex(String edgeClass,
      Vertex relationshipVertex, Vertex KRVertex)
  {
    String edgeQuery = "select from " + edgeClass + " where in = " + relationshipVertex.getId()
        + " and out = " + KRVertex.getId();
    Iterable<Edge> relationshipInheritanceEdgeResult = UtilClass.getGraph()
        .command(new OCommandSQL(edgeQuery))
        .execute();
    return relationshipInheritanceEdgeResult.iterator()
        .next();
  }
  
  public static Vertex getKRNodeBetweenNatureKlassNodeAndRelationshipId(Vertex klassNode,
      String relationshipId)
  {
    String krQuery = "select expand(out('" + RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF
        + "')[propertyId = '" + relationshipId + "']) from " + klassNode.getId();
    Iterable<Vertex> KRNodeResult = UtilClass.getGraph()
        .command(new OCommandSQL(krQuery))
        .execute();
    return KRNodeResult.iterator()
        .next();
  }
  
  /**
   * Gets all the eligible side1 klass Nature Nodes from side2 klass ids which
   * can be flowed into it.
   *
   * @param klassIds
   * @return
   */
  public static Iterable<Vertex> getOtherSideEligibleNatureKRNodesFromKlassIds(
      List<String> klassIds)
  {
    boolean isMarkerPresent = klassIds.remove(SystemLevelIds.MARKER);
    String finalQuery = "";
    String quotedKlassIds = EntityUtil.quoteIt(klassIds);
    String quotedRelationshipTypesToTransfer = EntityUtil
        .quoteIt(CommonConstants.RELATIONSHIP_TYPES_TO_TRANSFER);
    String otherSideKrQuery = "select expand(out('" + RelationshipLabelConstants.HAS_KLASS_PROPERTY
        + "')[" + CommonConstants.SIDE_PROPERTY + " = '" + CommonConstants.RELATIONSHIP_SIDE_2
        + "'].out('" + RelationshipLabelConstants.HAS_PROPERTY + "')["
        + IKlassNatureRelationship.RELATIONSHIP_TYPE + " in " + quotedRelationshipTypesToTransfer
        + "].in('" + RelationshipLabelConstants.HAS_PROPERTY + "')[" + CommonConstants.SIDE_PROPERTY
        + "= '" + CommonConstants.RELATIONSHIP_SIDE_1 + "']) from (" + "select from "
        + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where code in " + quotedKlassIds + ")";
    String markerQuery = "select expand(out('" + RelationshipLabelConstants.HAS_KLASS_PROPERTY
        + "')[" + CommonConstants.SIDE_PROPERTY + "= '" + CommonConstants.RELATIONSHIP_SIDE_2
        + "'].out('" + RelationshipLabelConstants.HAS_PROPERTY + "').in('"
        + RelationshipLabelConstants.HAS_PROPERTY + "')[" + CommonConstants.SIDE_PROPERTY + "= '"
        + CommonConstants.RELATIONSHIP_SIDE_1 + "'].in('"
        + RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF + "')[code in " + quotedKlassIds
        + " and isNature = true].out('" + RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF
        + "')) from (select from " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where code ='"
        + SystemLevelIds.MARKER + "')";
    if (isMarkerPresent) {
      finalQuery = "select expand($c) " + "let $a = (" + markerQuery + ")," + "$b = ("
          + otherSideKrQuery + ")," + "$c=unionall($a,$b)";
    }
    else {
      finalQuery = otherSideKrQuery;
    }
    
    return UtilClass.getGraph()
        .command(new OCommandSQL(finalQuery))
        .execute();
  }
  
  /**
   * Gets all the eligible side1 klass Nature Nodes from side1 klass id's which
   * can be flowed into side2 contents.
   *
   * @param klassIds
   * @return
   */
  public static Iterable<Vertex> getSameSideEligibleNatureKRNodesFromKlassIds(List<String> klassIds)
  {
    String quotedKlassIds = EntityUtil.quoteIt(klassIds);
    String natureRelationshipKRQuery = "select expand(out('"
        + RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF + "')["
        + CommonConstants.SIDE_PROPERTY + " = '" + CommonConstants.RELATIONSHIP_SIDE_1
        + "']) from (" + "select from klass where code in " + quotedKlassIds + ")";
    
    return UtilClass.getGraph()
        .command(new OCommandSQL(natureRelationshipKRQuery))
        .execute();
  }
  
  public static Iterable<Vertex> getNatureRelationshipNodesFromSide1KlassIds(List<String> klassIds)
  {
    String quotedKlassIds = EntityUtil.quoteIt(klassIds);
    String natureRelationshipKRQuery = "select expand(out('"
        + RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF + "')["
        + CommonConstants.SIDE_PROPERTY + " = '" + CommonConstants.RELATIONSHIP_SIDE_1 + "'].out('"
        + RelationshipLabelConstants.HAS_PROPERTY + "')) from (" + "select from "
        + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where code in " + quotedKlassIds + ")";
    
    return UtilClass.getGraph()
        .command(new OCommandSQL(natureRelationshipKRQuery))
        .execute();
  }
  
  public static Iterable<Vertex> getNatureRelationshipNodesFromSide2KlassIds(List<String> klassIds)
  {
    String quotedKlassIds = EntityUtil.quoteIt(klassIds);
    String natureRelationshipKRQuery = "select expand(out('"
        + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "')[" + CommonConstants.SIDE_PROPERTY
        + " = '" + CommonConstants.RELATIONSHIP_SIDE_2 + "'].out('"
        + RelationshipLabelConstants.HAS_PROPERTY + "')) from (" + "select from "
        + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where code in " + quotedKlassIds + ")";
    
    return UtilClass.getGraph()
        .command(new OCommandSQL(natureRelationshipKRQuery))
        .execute();
  }
  
  public static Iterable<Vertex> getNatureRelationshipNodesForGivenRelationshipIdsAndKlassIds(
      List<String> klassIds, List<String> relationshipIds)
  {
    String quotedklassIds = EntityUtil.quoteIt(klassIds);
    String quotedRelationshipIds = EntityUtil.quoteIt(relationshipIds);
    
    String natureRelationshipQuery = "select expand(in('"
        + RelationshipLabelConstants.HAS_INHERITANCE_RELATIONSHIP + "').out('"
        + RelationshipLabelConstants.HAS_PROPERTY + "')[side1.klassId in " + quotedklassIds
        + "]) from (select from " + VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP + " where "
        + CommonConstants.CODE_PROPERTY + " in " + quotedRelationshipIds + ")";
    
    return UtilClass.getGraph()
        .command(new OCommandSQL(natureRelationshipQuery))
        .execute();
  }
  
  public static Iterable<Vertex> getNatureRelationshipNodesForGivenKlassIds(List<String> klassIds)
  {
    String quotedklassIds = EntityUtil.quoteIt(klassIds);
    
    String natureRelationshipQuery = "select expand(in('"
        + RelationshipLabelConstants.HAS_INHERITANCE_RELATIONSHIP + "').out('"
        + RelationshipLabelConstants.HAS_PROPERTY + "')[side1.klassId in " + quotedklassIds
        + "]) from (select from " + VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP + ")";
    
    return UtilClass.getGraph()
        .command(new OCommandSQL(natureRelationshipQuery))
        .execute();
  }
  
  public static Iterable<Vertex> getSide2KlassNodesOfNatureRelationship(
      Vertex natureRelationshipNode)
  {
    String side2KlassIdsQuery = "select expand(in('" + RelationshipLabelConstants.HAS_PROPERTY
        + "')[" + CommonConstants.SIDE_PROPERTY + "= '" + CommonConstants.RELATIONSHIP_SIDE_2
        + "'].in('" + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "')) from "
        + natureRelationshipNode;
    
    Iterable<Vertex> side2KlassNodes = UtilClass.getGraph()
        .command(new OCommandSQL(side2KlassIdsQuery))
        .execute();
    return side2KlassNodes;
  }
  
  public static Iterable<Vertex> getSide1KlassNodesOfNatureRelationship(
      Vertex natureRelationshipNode)
  {
    String side2KlassIdsQuery = "select expand(in('" + RelationshipLabelConstants.HAS_PROPERTY
        + "')[" + CommonConstants.SIDE_PROPERTY + "= '" + CommonConstants.RELATIONSHIP_SIDE_1
        + "'].in('" + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "')) from "
        + natureRelationshipNode;
    
    Iterable<Vertex> side2KlassNodes = UtilClass.getGraph()
        .command(new OCommandSQL(side2KlassIdsQuery))
        .execute();
    return side2KlassNodes;
  }
  
  public static Iterable<Edge> getRelationshipInheritanceEdgesFromNatureRelationshipNode(
      Vertex natureRelationshipNode)
  {
    String relationshipInheritanceQuery = "select expand(in('"
        + RelationshipLabelConstants.HAS_PROPERTY + "')[side = 'side1'].outE('"
        + RelationshipLabelConstants.HAS_INHERITANCE_RELATIONSHIP + "'))from "
        + natureRelationshipNode.getId();
    Iterable<Edge> relationshipInheritanceEdges = UtilClass.getGraph()
        .command(new OCommandSQL(relationshipInheritanceQuery))
        .execute();
    return relationshipInheritanceEdges;
  }
  
  public static Iterable<Vertex> getPropagableRelationshipNodesFromNatureRelationshipIds(
      Collection<String> natureRelationshipIds)
  {
    String quotedNatureRelationshipIds = EntityUtil.quoteIt(natureRelationshipIds);
    String relationshipInheritanceQuery = "select expand(in('"
        + RelationshipLabelConstants.HAS_PROPERTY + "')[side = 'side1'].out('"
        + RelationshipLabelConstants.HAS_INHERITANCE_RELATIONSHIP + "'))from (select from "
        + VertexLabelConstants.NATURE_RELATIONSHIP + " where " + CommonConstants.CODE_PROPERTY
        + " in " + quotedNatureRelationshipIds + ")";
    Iterable<Vertex> propagableRelationshipNodes = UtilClass.getGraph()
        .command(new OCommandSQL(relationshipInheritanceQuery))
        .execute();
    return propagableRelationshipNodes;
  }
  
  public static Iterable<Edge> getSide2KREdgesOfNatureRelationshipFromSide1klassId(Object rid)
  {
    String query = "select expand(out('" + RelationshipLabelConstants.HAS_KLASS_PROPERTY
        + "')[type = '" + CommonConstants.RELATIONSHIP + "' and " + CommonConstants.SIDE_PROPERTY
        + " = '" + CommonConstants.RELATIONSHIP_SIDE_2 + "'].out('"
        + RelationshipLabelConstants.HAS_PROPERTY + "')" + "[" + IRelationship.IS_NATURE
        + " = 'true' and " + ProcessConstants.RELATIONSHIP_TYPE
        + " = 'productVariantRelationship' ].in('" + RelationshipLabelConstants.HAS_PROPERTY + "')["
        + CommonConstants.SIDE_PROPERTY + "= '" + CommonConstants.RELATIONSHIP_SIDE_2 + "'].inE('"
        + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "')) from" + rid;
    // String versionContextQuery = "select
    // expand(out('Klass_Nature_Relationship_Of').out('Has_Property')[side2.klassId
    // =
    // 'marker'].in('Has_Property')[side = 'side2'].inE('Has_Klass_Property'))
    // from"+rid;
    /*String finalQuery = "select expand($c) "
    + "let $a = ("+ query +"),"
    + "$b = ("+ versionContextQuery +"),"
    + "$c=unionall($a,$b)";*/
    Iterable<Edge> side2NatureRelationshipEdges = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    return side2NatureRelationshipEdges;
  }
  
  public static List<String> fetchSide2LinkedVariantKrIds(String subQuery)
  {
    String query = "Select expand(out('" + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "')["
        + CommonConstants.SIDE_PROPERTY + "= '" + IRelationship.SIDE2 + "'].out('"
        + RelationshipLabelConstants.HAS_PROPERTY + "')[" + IKlassNatureRelationship.IS_NATURE
        + "= true and " + IKlassNatureRelationship.RELATIONSHIP_TYPE + "= '"
        + CommonConstants.PRODUCT_VARIANT_RELATIONSHIP + "'].in('"
        + RelationshipLabelConstants.HAS_PROPERTY + "')[" + CommonConstants.SIDE_PROPERTY + "= '"
        + IRelationship.SIDE2 + "']) from " + subQuery;
    Iterable<Vertex> side2KrVertices = UtilClass.getVerticesFromQuery(query);
    return UtilClass.getCodes(side2KrVertices);
  }
  
  public static List<String> fetchAllSide2LinkedVariantKrIds()
  {
    String query = "Select expand(in('" + RelationshipLabelConstants.HAS_PROPERTY + "')[side = '"
        + IKlassNatureRelationship.SIDE2 + "']) from " + VertexLabelConstants.NATURE_RELATIONSHIP
        + " where " + IKlassNatureRelationship.RELATIONSHIP_TYPE + " = '"
        + CommonConstants.PRODUCT_VARIANT_RELATIONSHIP + "'";
    Iterable<Vertex> side2KrVertices = UtilClass.getVerticesFromQuery(query);
    return UtilClass.getCodes(side2KrVertices);
  }
  
  public static List<Long> fetchAllLinkedVariantPropertyIids()
  {
    String query = "Select * from " + VertexLabelConstants.NATURE_RELATIONSHIP
        + " where " + IKlassNatureRelationship.RELATIONSHIP_TYPE + " = '"
        + CommonConstants.PRODUCT_VARIANT_RELATIONSHIP + "'";
    Iterable<Vertex> relationshipVertices = UtilClass.getVerticesFromQuery(query);
    return UtilClass.getPropertyIIDs(relationshipVertices);
  }
  
  public static List<String> fetchAllLinkedVariantCodes()
  {
    String query = "Select * from " + VertexLabelConstants.NATURE_RELATIONSHIP
        + " where " + IKlassNatureRelationship.RELATIONSHIP_TYPE + " = '"
        + CommonConstants.PRODUCT_VARIANT_RELATIONSHIP + "'";
    Iterable<Vertex> relationshipVertices = UtilClass.getVerticesFromQuery(query);
    return UtilClass.getCodes(relationshipVertices);
  }
  
  public static List<String> fetchApplicableLinkedVariantCodes(String subQuery)
  {
    String query = "Select expand(out('" + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "')["
        + CommonConstants.SIDE_PROPERTY + "= '" + IRelationship.SIDE2 + "'].out('"
        + RelationshipLabelConstants.HAS_PROPERTY + "')[" + IKlassNatureRelationship.IS_NATURE
        + "= true and " + IKlassNatureRelationship.RELATIONSHIP_TYPE + "= '"
        + CommonConstants.PRODUCT_VARIANT_RELATIONSHIP + "']) from " + subQuery;
    Iterable<Vertex> linkedVariantRelationshipVertices = UtilClass.getVerticesFromQuery(query);
    return UtilClass.getCodes(linkedVariantRelationshipVertices);
  }
}
