package com.cs.runtime.strategy.plugin.usecase.klassinstance.clone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IKlassRelationshipSide;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCreateBulkCloneModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractGetConfigDetails;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForCreateBulkClone extends AbstractGetConfigDetails {
  
  public GetConfigDetailsForCreateBulkClone(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForCreateBulkClone/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    return getConfigDetails(requestMap);
  }
  
  protected Map<String, Object> getConfigDetails(Map<String, Object> requestMap) throws Exception
  {
    List<String> klassIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.KLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.SELECTED_TAXONOMY_IDS);
    List<String> klassAndTaxonomyIds = new ArrayList<>(klassIds);
    klassAndTaxonomyIds.addAll(taxonomyIds);
    Map<String, Object> mapToReturn = getMapToReturn();
    fillRelationshipsProperties(klassAndTaxonomyIds, mapToReturn);
    return mapToReturn;
  }
  
  protected Map<String, Object> getMapToReturn()
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    Map<String, Object> referencedRelationshipPropertiesMap = new HashMap<>();
    mapToReturn.put(IGetConfigDetailsForCreateBulkCloneModel.REFERENCED_RELATIONSHIPS_PROPERTIES,
        referencedRelationshipPropertiesMap);
    mapToReturn.put(IGetConfigDetailsForCreateBulkCloneModel.SIDE_IDS, new ArrayList<>());
    return mapToReturn;
  }
  
  /**
   * Description : For all klassIds passed, get all relationship nodes
   * connected, fetch respective side KR nodes and fill
   * referencedRelationshipProperties map\ with attribute/tags info, klassIds
   * that are connected to kR Nodes and targetTypes.
   *
   * @author Ajit
   * @param klassIds
   * @param mapToReturn
   * @throws Exception
   */
  protected void fillRelationshipsProperties(List<String> klassIds, Map<String, Object> mapToReturn)
      throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> referencedRelationshipProperties = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCreateBulkCloneModel.REFERENCED_RELATIONSHIPS_PROPERTIES);
    List<String> sideIds = (List<String>) mapToReturn
        .get(IGetConfigDetailsForCreateBulkCloneModel.SIDE_IDS);
    
    String query = "select from (select expand(in('has_property')) from "
        + VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP
        + ") where in('has_klass_property') contains (code in " + EntityUtil.quoteIt(klassIds)
        + ")";
    Iterable<Vertex> iterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex kRNode : iterable) {
      Map<String, Object> relationshipSide = kRNode.getProperty(CommonConstants.RELATIONSHIP_SIDE);
      String cardinality = (String) relationshipSide.get(IKlassRelationshipSide.CARDINALITY);
      Iterator<Vertex> relationshipIterator = kRNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      Vertex relationshipNode = relationshipIterator.next();
      String relationhipId = UtilClass.getCodeNew(relationshipNode);
      String sourceCardinality = (String) relationshipSide
          .get(IKlassRelationshipSide.SOURCE_CARDINALITY);
      if ((sourceCardinality.equals(CommonConstants.CARDINALITY_ONE)
          && cardinality.equals(CommonConstants.CARDINALITY_ONE))
          || relationshipNode.getProperty("@class")
              .equals(VertexLabelConstants.NATURE_RELATIONSHIP)
          || relationhipId.equals(SystemLevelIds.STANDARD_ARTICLE_GOLDEN_ARTICLE_RELATIONSHIP_ID)) {
        continue;
      }
      if (!sourceCardinality.equals(CommonConstants.CARDINALITY_ONE)) {
        sideIds.add(UtilClass.getCodeNew(kRNode));
      }
      String relationshipId = UtilClass.getCodeNew(relationshipNode);
      String label = (String) UtilClass.getValueByLanguage(relationshipNode,
          CommonConstants.LABEL_PROPERTY);
      Map<String, Object> relationshipPropertiesMap = new HashMap<>();
      relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.LABEL, label);
      RelationshipUtils.populatePropetiesInfoNew(relationshipNode, relationshipPropertiesMap);
      referencedRelationshipProperties.put(relationshipId, relationshipPropertiesMap);
    }
  }
}
