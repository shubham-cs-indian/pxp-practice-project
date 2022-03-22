package com.cs.di.runtime.entity.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.fasterxml.jackson.core.type.TypeReference;

public class PartnerAuthorizationDAO implements IPartnerAuthorizationDAO {
  
  private String GET_PARTNER_AUTHORIZATION_MAPPING = "GetPartnerAuthorizationMapping";
 
  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> getPartnerAuthMapping(String authMappingId, String localeId , boolean includeChildTaxonomies)
  {
    JSONObject requestModel = new JSONObject();
    requestModel.put(CommonConstants.ID_PROPERTY, authMappingId);
    requestModel.put(CommonConstants.INCLUDE_CHILD_TAXONOMIES, true);
    JSONObject authorizationMappings = null;
    try {
      authorizationMappings = CSConfigServer.instance().request(requestModel, GET_PARTNER_AUTHORIZATION_MAPPING, localeId);
    }
    catch (CSFormatException | CSInitializationException e) {
     RDBMSLogger.instance().exception(e);
    }
    
    return authorizationMappings;
  }
  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> getPartnerAuthMapping(String authMappingId, String localeId){
    
    return getPartnerAuthMapping(authMappingId,localeId, false);
  }
  @SuppressWarnings("unchecked")
  @Override
  public List<String> addAuthorizedChildTaxonomies(Set<String> taxonomyIds, String localeId, Map<String, Object> partnerAuthorization)
  {
    JSONObject requestModel = new JSONObject();
    JSONArray taxonomyIdsArray = new JSONArray();
    taxonomyIdsArray.addAll(taxonomyIds);
    requestModel.put(IListModel.LIST, taxonomyIdsArray);
    JSONObject parentVsChildTaxonomyModel = null;
    List<String> authorizedTaxonomies = new ArrayList<>();
    try {
      parentVsChildTaxonomyModel = CSConfigServer.instance().request(requestModel, "GetParentVsChildsTaxonomyIds", localeId);
      
      Map<String, Object> parentVsChildTaxonomyMap = (Map<String, Object>) parentVsChildTaxonomyModel.get("taxonomyMap");
      if (parentVsChildTaxonomyMap != null) {
        for (Object rootTaxonomy : parentVsChildTaxonomyMap.keySet()) {
          if (((List<String>) partnerAuthorization.get("taxonomyMappings")).contains(rootTaxonomy)) {
            authorizedTaxonomies.addAll(ObjectMapperUtil.readValue(parentVsChildTaxonomyMap.get(rootTaxonomy).toString(), 
                new TypeReference<List<String>>(){}));
          }
        }
      }
    }
    catch (CSFormatException | CSInitializationException | IOException e) {
      RDBMSLogger.instance().exception(e);
    }
    
    return authorizedTaxonomies;
  }
}
