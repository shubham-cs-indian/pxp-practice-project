package com.cs.core.bgprocess.idto;

import java.util.List;

public interface ITaxonomyInheritanceOnTypeSwitchDTO extends IInitializeBGProcessDTO {
  
  public static final String SOURCE_ENTITY_IID            = "sourceEntityIID";
  public static final String RELATIONSHIP_ID              = "relationshipId";
  public static final String TAXONOMY_INHERITANCE_SETTING = "taxonomyInheritanceSetting";
  public static final String ADDED_TAXONOMY_IDS           = "addedTaxonomyIds";
  public static final String REMOVED_TAXONOMY_IDS         = "removedTaxonomyIds";
  
  public Long getSourceEntityIID();
  
  public void setSourceEntityIID(Long sourceEntityIID);
    
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public String getTaxonomyInheritanceSetting();
  
  public void setTaxonomyInheritanceSetting(String taxonomyInheritanceSetting);
 
  public List<String> getAddedTaxonomyIds();
  
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds);
  
  public List<String> getRemovedTaxonomyIds();
  
  public void setRemovedTaxonomyIds(List<String> removedTaxonomyIds);
}
