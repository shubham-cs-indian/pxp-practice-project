package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.config.interactor.model.relationship.IRelationshipInformationModel;
import com.cs.core.config.interactor.model.relationship.RelationshipInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashSet;
import java.util.Set;

public class ConfigDetailsToGetCollectionInstancesResponseModel
    implements IConfigDetailsToGetCollectionInstancesResponseModel {
  
  private static final long               serialVersionUID = 1L;
  
  protected IRelationshipInformationModel scopePromotionRelationshipInfo;
  protected Set<String>                   klassIdsHavingRP = new HashSet<>();;
  protected Set<String>                   entities         = new HashSet<>();;
  protected String                        currentUserId;
  protected IRelationshipInformationModel subPromotionRelationshipInfo;
  protected Set<String>                   taxonomyIdsHavingRP;
  
  @Override
  public IRelationshipInformationModel getScopePromotionRelationshipInfo()
  {
    return scopePromotionRelationshipInfo;
  }
  
  @JsonDeserialize(as = RelationshipInformationModel.class)
  @Override
  public void setScopePromotionRelationshipInfo(
      IRelationshipInformationModel scopePromotionRelationshipInfo)
  {
    this.scopePromotionRelationshipInfo = scopePromotionRelationshipInfo;
  }
  
  @Override
  public Set<String> getKlassIdsHavingRP()
  {
    return klassIdsHavingRP;
  }
  
  @Override
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingRP)
  {
    this.klassIdsHavingRP = klassIdsHavingRP;
  }
  
  @Override
  public Set<String> getEntities()
  {
    return entities;
  }
  
  @Override
  public void setEntities(Set<String> entities)
  {
    this.entities = entities;
  }
  
  @Override
  public String getCurrentUserId()
  {
    return currentUserId;
  }
  
  @Override
  public void setCurrentUserId(String currentUserId)
  {
    this.currentUserId = currentUserId;
  }
  
  @Override
  public IRelationshipInformationModel getSubPromotionRelationshipInfo()
  {
    return subPromotionRelationshipInfo;
  }
  
  @JsonDeserialize(as = RelationshipInformationModel.class)
  @Override
  public void setSubPromotionRelationshipInfo(
      IRelationshipInformationModel subPromotionRelationshipInfo)
  {
    this.subPromotionRelationshipInfo = subPromotionRelationshipInfo;
  }
  
  @Override
  public Set<String> getTaxonomyIdsHavingRP()
  {
    if (taxonomyIdsHavingRP == null) {
      taxonomyIdsHavingRP = new HashSet<String>();
    }
    return taxonomyIdsHavingRP;
  }
  
  @Override
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP)
  {
    this.taxonomyIdsHavingRP = taxonomyIdsHavingRP;
  }
}
