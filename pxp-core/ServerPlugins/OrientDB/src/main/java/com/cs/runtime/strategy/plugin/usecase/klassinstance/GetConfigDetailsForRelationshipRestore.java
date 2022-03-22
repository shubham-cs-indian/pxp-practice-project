package com.cs.runtime.strategy.plugin.usecase.klassinstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.klass.util.ConfigDetailsUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.ReferencedRelationshipUtil;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractGetConfigDetails;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForRelationshipRestore extends AbstractGetConfigDetails {
  
  public GetConfigDetailsForRelationshipRestore(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForRelationshipRestore/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = getMapToReturn();
    
    List<String> klassIds = (List<String>) requestMap
        .get(IGetConfigDetailsForSaveRelationshipInstancesRequestModel.KLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestMap
        .get(IGetConfigDetailsForSaveRelationshipInstancesRequestModel.TAXONOMY_IDS);
    
    if (klassIds.isEmpty() && taxonomyIds.isEmpty()) {
      return mapToReturn;
    }
    
    fillReferencedRelationshipsAndElementsForTypes(mapToReturn, klassIds);
    fillReferencedRelationshipsAndElementsForTaxonomies(mapToReturn, taxonomyIds);
    
    ConfigDetailsUtils.fillSide2LinkedVariantKrIds(mapToReturn);
    return mapToReturn;
  }
  
  protected void fillReferencedRelationshipsAndElementsForTypes(Map<String, Object> mapToReturn,
      List<String> klassIds) throws Exception
  {
    Iterable<Vertex> klassIterator = UtilClass.getVerticesByIndexedIds(klassIds,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    for (Vertex klassNode : klassIterator) {
      Boolean isNature = klassNode.getProperty(IKlass.IS_NATURE);
      if (isNature) {
        fillReferencedNatureRelationshipsAndElements(mapToReturn, klassNode);
      }
      fillReferencedRelationshipsAndElements(mapToReturn, klassNode);
    }
  }
  
  protected void fillReferencedRelationshipsAndElementsForTaxonomies(
      Map<String, Object> mapToReturn, List<String> taxonomyIds) throws Exception
  {
    Iterable<Vertex> taxonomyIterator = UtilClass.getVerticesByIndexedIds(taxonomyIds,
        VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    for (Vertex taxonomyNode : taxonomyIterator) {
      fillReferencedRelationshipsAndElements(mapToReturn, taxonomyNode);
    }
  }
  
  private void fillReferencedNatureRelationshipsAndElements(Map<String, Object> mapToReturn,
      Vertex klassNode) throws Exception
  {
    Map<String, Object> referencedRelationshipMap = ReferencedRelationshipUtil
        .getReferencedRelationships(mapToReturn, true);
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    Iterable<Vertex> klassNatureRelationshipOfVertices = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF,
        RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    for (Vertex klassNatureRelationshipVertex : klassNatureRelationshipOfVertices) {
      Iterable<Vertex> natureRelationshipNodes = klassNatureRelationshipVertex
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY);
      for (Vertex natureRelationshipNode : natureRelationshipNodes) {
        if (natureRelationshipNode.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY)
            .equals(VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP)
            || natureRelationshipNode.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY)
                .equals(VertexLabelConstants.NATURE_RELATIONSHIP)) {
          String relationshipId = UtilClass.getCodeNew(natureRelationshipNode);
          Map<String, Object> relationshipMap = RelationshipUtils
              .getRelationshipMap(natureRelationshipNode);
          referencedRelationshipMap.put(relationshipId, relationshipMap);
          ReferencedRelationshipUtil.fillReferencedRelationshipElements(natureRelationshipNode,
              referencedElements);
        }
      }
    }
  }
  
  private void fillReferencedRelationshipsAndElements(Map<String, Object> mapToReturn,
      Vertex klassNode) throws Exception
  {
    String query = "select expand( out(" + RelationshipLabelConstants.HAS_KLASS_PROPERTY
        + ")[type = 'relationship'].out(" + RelationshipLabelConstants.HAS_PROPERTY
        + ")[isNature = false])  from" + klassNode.getId();
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    Map<String, Object> referencedRelationshipMap = ReferencedRelationshipUtil
        .getReferencedRelationships(mapToReturn, false);
    Iterable<Vertex> relationshipNodes = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex relationshipNode : relationshipNodes) {
      String relationshipId = UtilClass.getCodeNew(relationshipNode);
      HashMap<String, Object> relationshipMap = RelationshipUtils
          .getRelationshipMap(relationshipNode);
      referencedRelationshipMap.put(relationshipId, relationshipMap);
      ReferencedRelationshipUtil.fillReferencedRelationshipElements(relationshipNode,
          referencedElements);
    }
  }
  
  protected Map<String, Object> getMapToReturn()
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(
        IGetConfigDetailsForSaveRelationshipInstancesResponseModel.REFERENCED_NATURE_RELATIONSHIPS,
        new HashMap<>());
    mapToReturn.put(IGetConfigDetailsForSaveRelationshipInstancesResponseModel.REFERENCED_ELEMENTS,
        new HashMap<>());
    mapToReturn.put(
        IGetConfigDetailsForSaveRelationshipInstancesResponseModel.REFERENCED_RELATIONSHIPS,
        new HashMap<>());
    return mapToReturn;
  }
}
