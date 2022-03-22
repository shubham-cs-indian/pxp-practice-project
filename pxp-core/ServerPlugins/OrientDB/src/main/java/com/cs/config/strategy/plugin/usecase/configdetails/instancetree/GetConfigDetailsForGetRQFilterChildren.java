package com.cs.config.strategy.plugin.usecase.configdetails.instancetree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.configdetails.instancetree.abstrct.AbstractGetConfigDetailsForGetFilterChildren;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRelationshipQuicklistRequestModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetConfigDetailsForGetRQFilterChildren extends AbstractGetConfigDetailsForGetFilterChildren {

  public GetConfigDetailsForGetRQFilterChildren(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] {"POST|GetConfigDetailsForGetRQFilterChildren/*"};
  }

  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    
    List<String> targetModuleEntities = getModuleEntitiesAndfillTargetIdsForRelationship(requestMap, mapToReturn);
    requestMap.put(IConfigDetailsForInstanceTreeGetRequestModel.ALLOWED_ENTITIES, targetModuleEntities);
    
    execute(requestMap, mapToReturn);
    fillRelationshipConfig((String) requestMap.get(IConfigDetailsForRelationshipQuicklistRequestModel.RELATIONSHIP_ID), mapToReturn);
    return mapToReturn;
  }  
}
