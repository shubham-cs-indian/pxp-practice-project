package com.cs.config.strategy.plugin.usecase.configdetails.instancetree.abstrct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRelationshipQuicklistRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public abstract class AbstractGetConfigDetailsForGetNewInstanceTree extends AbstractConfigDetailsForNewInstanceTree {
  
  public AbstractGetConfigDetailsForGetNewInstanceTree(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  protected void execute(Map<String, Object> requestMap, Map<String, Object> mapToReturn) throws Exception
  {
    String userId = (String) requestMap.get(IConfigDetailsForInstanceTreeGetRequestModel.USER_ID);
    Vertex roleNode = RoleUtils.getRoleFromUser(userId);


    managePermission(roleNode, requestMap, mapToReturn);

    List<String> allowedEntities = (List<String>) mapToReturn.get(IConfigDetailsForGetNewInstanceTreeModel.ALLOWED_ENTITIES);
    List<String> searchableAttributes = getSearchableAttributes(allowedEntities);
    List<String> translatableAttributes = getTranslatableAttributes(allowedEntities);

    /*List<String> attributeIds = (List<String>) requestMap.get(IGetConfigDetailsForGetNewInstanceTreeRequestModel.ATTRIBUTE_IDS);
    attributeIds.addAll(searchableAttributes);*/
    fillReferencedProperties(requestMap, mapToReturn);
    List<Map<String, Object>> filterData = getFilterDataForSelectedModule(requestMap, allowedEntities);
    mapToReturn.put(IConfigDetailsForGetNewInstanceTreeModel.SEARCHABLE_ATTRIBUTE_IDS, searchableAttributes);
    mapToReturn.put(IConfigDetailsForGetNewInstanceTreeModel.TRANSLATABLE_ATTRIBUTE_IDS, translatableAttributes);
    mapToReturn.put(IConfigDetailsForGetNewInstanceTreeModel.FILTER_DATA, filterData);
    fillRelationshipConfig((String) requestMap.get(IConfigDetailsForRelationshipQuicklistRequestModel.RELATIONSHIP_ID), mapToReturn);
  }
  
  public static List<Map<String, Object>> getFilterDataForSelectedModule(Map<String, Object> requestMap, 
      List<String> allowedEntities) throws Exception 
  {
    Boolean isFilterDataRequired = (Boolean) requestMap
        .get(IGetConfigDetailsForGetNewInstanceTreeRequestModel.IS_FILTER_DATA_REQUIRED);
    
    List<Map<String, Object>> filterData = new ArrayList<>();
    if(isFilterDataRequired) {
      fillFilterData(allowedEntities, filterData);
    }

    return filterData;
  }
}
