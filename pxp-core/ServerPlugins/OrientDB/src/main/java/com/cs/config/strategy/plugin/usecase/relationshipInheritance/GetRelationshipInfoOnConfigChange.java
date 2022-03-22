package com.cs.config.strategy.plugin.usecase.relationshipInheritance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.entity.relationshipinstance.IConfigChangeRelationshipInheritanceModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel;
import com.cs.runtime.strategy.plugin.usecase.relationshipInheritance.util.RelationshipInheritanceUtil;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetRelationshipInfoOnConfigChange extends AbstractOrientPlugin {
  
  public GetRelationshipInfoOnConfigChange(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRelationshipInfoOnConfigChange/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> referencedRelationshipsMap = new HashMap<>();
    Map<String, Object> returnMap = new HashMap<>();
    List<String> relationshipIds = (List<String>) requestMap.get(IConfigChangeRelationshipInheritanceModel.RELATIONSHIP_IDS);
    
    for (String relationshipId : relationshipIds) {
      RelationshipInheritanceUtil
          .getRelationshipMapForRelationshipInheritance(referencedRelationshipsMap, relationshipId);
    }
    returnMap.put(
        IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel.REFERENCED_RELATIONSHIP,
        referencedRelationshipsMap);
    
    return returnMap;
  }
}
