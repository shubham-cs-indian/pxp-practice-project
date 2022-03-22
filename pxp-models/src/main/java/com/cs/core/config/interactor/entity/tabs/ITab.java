package com.cs.core.config.interactor.entity.tabs;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;

import java.util.List;

public interface ITab extends IConfigMasterEntity {
  
  public static final String PROPERTY_SEQUENCE_LIST  = "propertySequenceList";
  public static final String PROPERTY_COLLECTION_IDS = "propertyCollectionIds";
  public static final String VARIANT_CONTEXT_IDS     = "variantContextIds";
  public static final String RELATIONSHIP_IDS        = "relationshipIds";
  public static final String IS_STANDARD             = "isStandard";
  
  public List<String> getPropertySequenceList();
  
  public void setPropertySequenceList(List<String> propertySequenceList);
  
  public List<String> getPropertyCollectionIds();
  
  public void setPropertyCollectionIds(List<String> propertyCollectionIds);
  
  public List<String> getVariantContextIds();
  
  public void setVariantContextIds(List<String> variantContextIds);
  
  public List<String> getRelationshipIds();
  
  public void setRelationshipIds(List<String> relationshipIds);
  
  public Boolean getIsStandard();
  
  public void setIsStandard(Boolean isStandard);
}
