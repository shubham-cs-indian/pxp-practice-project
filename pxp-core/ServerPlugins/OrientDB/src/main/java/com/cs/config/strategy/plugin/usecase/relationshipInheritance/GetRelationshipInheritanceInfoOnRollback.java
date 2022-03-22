package com.cs.config.strategy.plugin.usecase.relationshipInheritance;

import com.cs.core.runtime.interactor.entity.relationshipinstance.IGetNatureRelationshipInfoForRollbackResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.ITypesTaxonomiesModel;
import com.cs.repository.relationship.RelationshipRepository;
import com.cs.runtime.strategy.plugin.usecase.relationshipInheritance.util.RelationshipInheritanceUtil;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetRelationshipInheritanceInfoOnRollback extends AbstractOrientPlugin {
  
  public GetRelationshipInheritanceInfoOnRollback(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRelationshipInheritanceInfoOnRollback/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> klassIds = (List<String>) requestMap.get(ITypesTaxonomiesModel.TYPES);
    
    Map<String, Object> parentReferencedNatureRelationships = new HashMap<>();
    Map<String, Object> parentReferencedRelationships = new HashMap<>();
    Map<String, Object> childReferencedNatureRelationships = new HashMap<>();
    Map<String, Object> childReferencedRelationships = new HashMap<>();
    
    Iterable<Vertex> parentNatureKRNodes = RelationshipRepository
        .getNatureRelationshipNodesFromSide1KlassIds(klassIds);
    fillInfo(parentReferencedNatureRelationships, parentReferencedRelationships,
        parentNatureKRNodes);
    Iterable<Vertex> childNatureKRNodes = RelationshipRepository
        .getNatureRelationshipNodesFromSide2KlassIds(klassIds);
    fillInfo(childReferencedNatureRelationships, childReferencedRelationships, childNatureKRNodes);
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(
        IGetNatureRelationshipInfoForRollbackResponseModel.PARENT_REFERENCED_NATURE_RELATIONSHIPS,
        parentReferencedNatureRelationships);
    returnMap.put(IGetNatureRelationshipInfoForRollbackResponseModel.PARENT_REFERENCED_RELATIONSHIP,
        parentReferencedRelationships);
    returnMap.put(
        IGetNatureRelationshipInfoForRollbackResponseModel.CHILD_REFERENCED_NATURE_RELATIONSHIPS,
        childReferencedNatureRelationships);
    returnMap.put(IGetNatureRelationshipInfoForRollbackResponseModel.CHILD_REFERENCED_RELATIONSHIP,
        childReferencedRelationships);
    return returnMap;
  }
  
  private void fillInfo(Map<String, Object> parentReferencedNatureRelationshipsMap,
      Map<String, Object> referencedRelationshipsMap, Iterable<Vertex> natureKRNodes)
      throws Exception
  {
    List<String> propagableRelationshipIds = RelationshipInheritanceUtil
        .fillReferencedNRForRelationshipInheritance(parentReferencedNatureRelationshipsMap,
            natureKRNodes);
    RelationshipInheritanceUtil.fillReferenceRelationshipInfoForRelationshipInheritance(
        propagableRelationshipIds, referencedRelationshipsMap);
  }
}
