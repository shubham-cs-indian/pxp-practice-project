package com.cs.config.strategy.plugin.usecase.configdetails.instancetree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.configdetails.instancetree.abstrct.AbstractGetConfigDetailsForFilterAndSortData;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRQFilterAndSortDataRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRelationshipQuicklistRequestModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class GetConfigDetailsForGetRQFilterAndSortData extends AbstractGetConfigDetailsForFilterAndSortData {
	  
	  public GetConfigDetailsForGetRQFilterAndSortData(final OServerCommandConfiguration iConfiguration)
	  {
	    super(iConfiguration);
	  }
	  
	  @Override
	  public String[] getNames()
	  {
	    return new String[] { "POST|GetConfigDetailsForGetRQFilterAndSortData/*" };
	  }

	  @Override
	  public Object execute(Map<String, Object> requestMap) throws Exception
	  {
	    Map<String, Object> mapToReturn = new HashMap<>();
	    
	    List<String> targetModuleEntities = getModuleEntitiesAndfillTargetIdsForRelationship(requestMap, mapToReturn);
	    requestMap.put(IConfigDetailsForRQFilterAndSortDataRequestModel.ALLOWED_ENTITIES, targetModuleEntities);
	    
	    execute(requestMap, mapToReturn);

	    fillRelationshipConfig((String) requestMap.get(IConfigDetailsForRelationshipQuicklistRequestModel.RELATIONSHIP_ID), mapToReturn);
	    
	    return mapToReturn;
	  }
}
