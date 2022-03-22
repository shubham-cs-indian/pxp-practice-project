package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.config.interactor.model.relationship.IRelationshipInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Set;

public interface IConfigDetailsToGetCollectionInstancesResponseModel extends IModel {
  
  public static final String SCOPE_PROMOTION_RELATIONSHIP_INFO = "scopePromotionRelationshipInfo";
  public static final String KLASS_IDS_HAVING_RP               = "klassIdsHavingRP";
  public static final String ENTITIES                          = "entities";
  public static final String CURRENT_USER_ID                   = "currentUserId";
  public static final String SUB_PROMOTION_RELATIONSHIP_INFO   = "subPromotionRelationshipInfo";
  public static final String TAXONOMY_IDS_HAVING_RP            = "taxonomyIdsHavingRP";
  
  public IRelationshipInformationModel getScopePromotionRelationshipInfo();
  
  public void setScopePromotionRelationshipInfo(
      IRelationshipInformationModel scopePromotionRelationshipInfo);
  
  public Set<String> getKlassIdsHavingRP();
  
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingRP);
  
  public Set<String> getEntities();
  
  public void setEntities(Set<String> entities);
  
  public String getCurrentUserId();
  
  public void setCurrentUserId(String currentUserId);
  
  public IRelationshipInformationModel getSubPromotionRelationshipInfo();
  
  public void setSubPromotionRelationshipInfo(
      IRelationshipInformationModel subPromotionRelationshipInfo);
  
  public Set<String> getTaxonomyIdsHavingRP();
  
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP);
}
