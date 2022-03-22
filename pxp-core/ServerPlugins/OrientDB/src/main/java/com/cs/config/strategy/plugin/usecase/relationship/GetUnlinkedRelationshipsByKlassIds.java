package com.cs.config.strategy.plugin.usecase.relationship;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.model.klass.IReferencedSectionRelationshipModel;
import com.cs.core.config.interactor.model.relationship.IGetReferencedRelationshipsAndElementsModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetUnlinkedRelationshipsByKlassIds extends AbstractOrientPlugin {
  
  public GetUnlinkedRelationshipsByKlassIds(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> relationshipIds = new ArrayList<String>();
    List<String> natureRelationshipIds = new ArrayList<String>();
    
    List<String> klassIds = (List<String>) requestMap.get("ids");
    String loginUserId = (String) requestMap.get("userId");
    
    Map<String, Object> relationshipsMap = new HashMap<>();
    Map<String, Object> normalRelationshipsMap = new HashMap<>();
    Map<String, Object> referencedElementMap = new HashMap<>();
    Map<String, String> referencedRelationshipsMapping = new HashMap<String, String>();
    Map<String, Object> referencedNatureRelationships = new HashMap<>();
    
    relationshipIds = RelationshipUtils.getRelationshipIdsNotLinkedWithKlass(klassIds,
        relationshipsMap, referencedElementMap, referencedRelationshipsMapping);
    
    for (String key : relationshipsMap.keySet()) {
      Map<String, Object> entityMap = (Map<String, Object>) relationshipsMap.get(key);
      if (entityMap.containsKey(IKlassNatureRelationship.RELATIONSHIP_TYPE)) {
        natureRelationshipIds.add(key);
        relationshipIds.remove(key);
        referencedNatureRelationships.put(key, entityMap);
      }
      else {
        normalRelationshipsMap.put(key, entityMap);
      }
      Map<String, Object> referencedElement = (Map<String, Object>) referencedElementMap.get(key);
      referencedElement.put(IReferencedSectionRelationshipModel.IS_LINKED, false);
    }
    
    List<String> klassIdsWithReadPermission = GlobalPermissionUtils.getKlassesForUser(loginUserId);
    
    Map<String, Object> response = new HashMap<>();
    response.put(IGetReferencedRelationshipsAndElementsModel.RELATIONSHIP_IDS, relationshipIds);
    response.put(IGetReferencedRelationshipsAndElementsModel.NATURE_RELATIONSHIP_IDS,
        natureRelationshipIds);
    response.put(IGetReferencedRelationshipsAndElementsModel.REFERENCED_RELATIONSHIPS,
        normalRelationshipsMap);
    response.put(IGetReferencedRelationshipsAndElementsModel.REFERENCED_NATURE_RELATIONSHIPS,
        referencedNatureRelationships);
    response.put(IGetReferencedRelationshipsAndElementsModel.REFERENCED_ELEMENTS,
        referencedElementMap);
    response.put(IGetReferencedRelationshipsAndElementsModel.KLASS_IDS, klassIdsWithReadPermission);
    response.put(IGetReferencedRelationshipsAndElementsModel.REFERENCED_RELATIONSHIPS_MAPPING,
        referencedRelationshipsMapping);
    
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetUnlinkedRelationshipsByKlassIds/*" };
  }
}
