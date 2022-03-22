package com.cs.core.runtime.interactor.model.dashboard;

import com.cs.core.runtime.interactor.model.searchable.IInstanceSearchModel;

import java.util.List;
import java.util.Set;

public interface IDashboardInformationRequestModel extends IInstanceSearchModel {
  
  public static final String CURRENT_USER_ID        = "currentUserId";
  public static final String MODULE_ENTITIES        = "moduleEntities";
  public static final String KLASS_IDS_HAVING_RP    = "klassIdsHavingRP";
  public static final String TAXONOMY_IDS_HAVING_RP = "taxonomyIdsHavingRP";
  public static final String MAJOR_TAXONOMY_IDS     = "majorTaxonomyIds";
  
  public String getCurrentUserId();
  
  public void setCurrentUserId(String currentUserId);
  
  public List<String> getModuleEntities();
  
  public void setModuleEntities(List<String> moduleEntities);
  
  public Set<String> getTaxonomyIdsHavingRP();
  
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP);
  
  public Set<String> getKlassIdsHavingRP();
  
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingRP);

  public List<String> getMajorTaxonomyIds();
  
  public void setMajorTaxonomyIds(List<String> majorTaxonomyIds);

}
