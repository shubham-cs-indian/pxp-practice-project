package com.cs.core.bgprocess.idto;

import java.util.List;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IRelationshipInheritanceOnTypeSwitchDTO extends IInitializeBGProcessDTO{
  
  public Long getContentId();
  public void setContentId(Long contentId);
  
  public List<String> getExistingTypes();
  public void setExistingTypes(List<String> existingTypes);
  
  public List<String> getExistingTaxonomies();
  public void setExistingTaxonomies(List<String> existingTaxonomies);
  
  public List<String> getAddedTypes();
  public void setAddedTypes(List<String> addedTypes);
  
  public List<String> getAddedTaxonomies();
  public void setAddedTaxonomies(List<String> addedTaxonomies);
  
  public List<String> getRemovedTypes();
  public void setRemovedTypes(List<String> removedTypes);
  
  public List<String> getRemovedTaxonomies();
  public void setRemovedTaxonomies(List<String> removedTaxonomies);
  
}
