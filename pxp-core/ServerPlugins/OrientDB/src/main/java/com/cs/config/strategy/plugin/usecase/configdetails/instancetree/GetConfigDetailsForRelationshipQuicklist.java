package com.cs.config.strategy.plugin.usecase.configdetails.instancetree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.configdetails.instancetree.abstrct.AbstractGetConfigDetailsForGetNewInstanceTree;
import com.cs.config.strategy.plugin.usecase.klass.util.ConfigDetailsUtils;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRelationshipQuicklistResponseModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetConfigDetailsForRelationshipQuicklist extends AbstractGetConfigDetailsForGetNewInstanceTree {

  public GetConfigDetailsForRelationshipQuicklist(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] {"POST|GetConfigDetailsForRelationshipQuicklist/*"};
  }

  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    
    List<String> targetModuleEntities = getModuleEntitiesAndfillTargetIdsForRelationship(requestMap, mapToReturn);
    requestMap.put(IConfigDetailsForInstanceTreeGetRequestModel.ALLOWED_ENTITIES, targetModuleEntities);
    ConfigDetailsUtils.fillLinkedVariantsConfigInfo(mapToReturn,
        (List<String>) mapToReturn.get(IConfigDetailsForRelationshipQuicklistResponseModel.TARGET_IDS));
    
    execute(requestMap, mapToReturn);
    
    return mapToReturn;
  }
}
