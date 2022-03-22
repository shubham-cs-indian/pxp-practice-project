package com.cs.config.strategy.plugin.usecase.configdetails.instancetree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.configdetails.instancetree.abstrct.AbstarctConfigDetailForKlassTaxonomyTree;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.Constants;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRelationshipKlassTaxonomyRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRelationshipKlassTaxonomyResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRelationshipQuicklistRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRelationshipQuicklistResponseModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class GetConfigDetailForRelationshipKlassTaxonomyTree extends AbstarctConfigDetailForKlassTaxonomyTree {
  
  public GetConfigDetailForRelationshipKlassTaxonomyTree(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = (Map<String, Object>) super.execute(requestMap);
    String relationshipId = (String)requestMap.get(IConfigDetailsForRelationshipKlassTaxonomyRequestModel.RELATIONSHIP_ID);
    fillRelationshipConfig(relationshipId, returnMap);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailForRelationshipKlassTaxonomyTree/*" };
  }
  
  @Override
  protected void prepareAllowedEntities(Map<String, Object> requestMap, List<String> allowedEntities, Map<String, Object> mapToReturn) throws Exception
  {
    String targetKlassId = (String) requestMap.get(IConfigDetailsForRelationshipKlassTaxonomyRequestModel.TARGET_ID);
    Set<String> allowedEntitiesInKlass = new HashSet<String>();
    List<String> targetIds = fillTargetIdsForRelationship(requestMap, targetKlassId, allowedEntitiesInKlass);
    allowedEntities.retainAll(allowedEntitiesInKlass);
    mapToReturn.put(IConfigDetailsForRelationshipKlassTaxonomyResponseModel.TARGET_IDS, targetIds);
  }
 
  
  protected List<String> fillTargetIdsForRelationship(Map<String, Object> requestMap, String targetId, Set<String> allowedEnities)
      throws Exception
  {
    String sideId = (String) requestMap.get(IConfigDetailsForRelationshipQuicklistRequestModel.SIDE_ID);
    List<String> targetIds = new ArrayList<>();
    String relationshipId = (String) requestMap.get(IConfigDetailsForRelationshipQuicklistRequestModel.RELATIONSHIP_ID);
    Vertex relationshipNode = UtilClass.getVertexByIndexedId(relationshipId, VertexLabelConstants.ROOT_RELATIONSHIP);
    Iterable<Vertex> kRNodes = relationshipNode.getVertices(Direction.IN, RelationshipLabelConstants.HAS_PROPERTY);
    for (Vertex kRNode : kRNodes) {
      if(sideId!=null && sideId.equals(UtilClass.getCId(kRNode))) {
        continue; //self side
      }
      Boolean foundTarget = false;
      Iterable<Vertex> klassNodes = kRNode.getVertices(Direction.IN, RelationshipLabelConstants.HAS_KLASS_PROPERTY);
      for (Vertex klassNode : klassNodes) {
        String klassId = UtilClass.getCId(klassNode);
        targetIds.add(klassId);
        String typeProperty = (String) klassNode.getProperty(Constants.TYPE);
        allowedEnities.add(EntityUtil.getModuleEntityFromKlassType(typeProperty));
        if(klassId.equals(targetId)) {
          foundTarget = true;
        }
      }
      if(foundTarget) {
        break;
      }
      targetIds.clear();
    }
    return targetIds;
  }
  
  protected void fillRelationshipConfig(String relationshipId, Map<String, Object> mapToReturn)
      throws Exception
  {
    if(relationshipId == null || relationshipId.isEmpty()) {
      return;
    }
    
    Vertex relationshipNode = null;
    try {
      relationshipNode = UtilClass.getVertexById(relationshipId,
          VertexLabelConstants.ROOT_RELATIONSHIP);
    }
    catch (NotFoundException e) {
      throw new NotFoundException();
    }
    List<String> relationshipFieldToFetch = Arrays.asList(IRelationship.ID,
        IRelationship.PROPERTY_IID, IRelationship.CODE, IRelationship.IS_NATURE,
        IRelationship.TYPE, IRelationship.SIDE1, IRelationship.SIDE2);
    Map<String, Object> referencedElement = UtilClass.getMapFromVertex(relationshipFieldToFetch,
        relationshipNode);
    mapToReturn.put(IConfigDetailsForRelationshipKlassTaxonomyResponseModel.RELATIONSHIP_CONFIG, referencedElement);
  }

}
