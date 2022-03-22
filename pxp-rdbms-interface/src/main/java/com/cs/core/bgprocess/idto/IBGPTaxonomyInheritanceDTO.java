package com.cs.core.bgprocess.idto;

import java.util.List;

public interface IBGPTaxonomyInheritanceDTO extends IInitializeBGProcessDTO {
  
  public static final String ADDED_ELEMENT_IIDS           = "addedElementIIDs";
  public static final String DELETED_ELEMENT_IIDS         = "deletedElementIIDs";
  public static final String SOURCE_ENTITY_IID            = "sourceEntityIID";
  public static final String PROPERTY_IID                 = "PropertyIID";
  public static final String TAXONOMY_INHERITANCE_SETTING = "taxonomyInheritanceSetting";

  public Long getSourceEntityIID();
  
  public void setSourceEntityIID(Long sourceEntityIID);
  
  public Long getPropertyIID();
  
  public void setPropertyIID(Long relationshipId);
  
  public String getTaxonomyInheritanceSetting();
  
  public void setTaxonomyInheritanceSetting(String taxonomyInheritanceSetting);
  
  public List<Long> getAddedElementIIDs();
  
  public void setAddedElementIIDs(List<Long> addedElementIIDs);
  
  public List<Long> getDeletedElementIIDs();
  
  public void setDeletedElementIIDs(List<Long> deletedElementIIDs);
}
