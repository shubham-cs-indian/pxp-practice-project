package com.cs.config.strategy.plugin.usecase.relationshipInheritance;

import com.cs.core.runtime.interactor.entity.relationshipinstance.IGetNatureRelationshipInfoForRelationshipInheritanceRequestModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel;
import com.cs.repository.relationship.RelationshipRepository;
import com.cs.runtime.strategy.plugin.usecase.relationshipInheritance.util.RelationshipInheritanceUtil;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetNatureRelationshipInfoForRelationshipInheritance extends AbstractOrientPlugin {
  
  public GetNatureRelationshipInfoForRelationshipInheritance(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetNatureRelationshipInfoForRelationshipInheritance/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> relationshipIds = (List<String>) requestMap
        .get(IGetNatureRelationshipInfoForRelationshipInheritanceRequestModel.RELATIONSHIP_IDS);
    List<String> klassIds = (List<String>) requestMap
        .get(IGetNatureRelationshipInfoForRelationshipInheritanceRequestModel.TYPES);
    
    Map<String, Object> returnMap = new HashMap<>();
    Map<String, Object> referencedNatureRelationshipsMap = new HashMap<>();
    Map<String, Object> referencedRelationshipsMap = new HashMap<>();
    
    Iterable<Vertex> natureRelationshipNodes = RelationshipRepository
        .getNatureRelationshipNodesForGivenRelationshipIdsAndKlassIds(klassIds, relationshipIds);
    
    RelationshipInheritanceUtil.fillReferencedNRForRelationshipInheritance(
        referencedNatureRelationshipsMap, natureRelationshipNodes);
    RelationshipInheritanceUtil.fillReferenceRelationshipInfoForRelationshipInheritance(
        relationshipIds, referencedRelationshipsMap);
    
    returnMap.put(
        IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel.REFERENCED_RELATIONSHIP,
        referencedRelationshipsMap);
    returnMap.put(
        IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel.REFERENCED_NATURE_RELATIONSHIPS,
        referencedNatureRelationshipsMap);
    return returnMap;
  }
}
