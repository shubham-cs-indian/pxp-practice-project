package com.cs.di.runtime.entity.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IPartnerAuthorizationDAO {
  
  /**
   * 
   * @param authMappingId
   * @param localeId
   * @return the partner authorization map to authorized a poperties while
   *         import.
   */
  public Map<String, Object> getPartnerAuthMapping(String authMappingId, String localeId);
  
  /**
   * @param taxonomyIds
   * @param localeId
   * @param partnerAuthorization 
   * @return
   */
  public List<String> addAuthorizedChildTaxonomies(Set<String> taxonomyIds, String localeId, Map<String, Object> partnerAuthorization);
  
  public Map<String, Object> getPartnerAuthMapping(String authMappingId, String localeId , boolean includeChildTaxonomies);
}
