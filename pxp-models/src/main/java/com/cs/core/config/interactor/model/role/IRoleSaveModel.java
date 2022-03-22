package com.cs.core.config.interactor.model.role;

import java.util.List;

public interface IRoleSaveModel extends IRoleModel {
  
  public static final String ADDED_ENDPOINTS           = "addedEndpoints";
  public static final String DELETED_ENDPOINTS         = "deletedEndpoints";
  public static final String ADDED_TARGET_KLASSES      = "addedTargetKlasses";
  public static final String DELETED_TARGET_KLASSES    = "deletedTargetKlasses";
  public static final String ADDED_TARGET_TAXONOMIES   = "addedTargetTaxonomies";
  public static final String DELETED_TARGET_TAXONOMIES = "deletedTargetTaxonomies";
  public static final String ADDED_KPIS                = "addedKPIs";
  public static final String DELETED_KPIS              = "deletedKPIs";
  public static final String DELETED_SYSTEM_IDS        = "deletedSystemIds";
  public static final String ADDED_SYSTEM_IDS          = "addedSystemIds";
  
  public List<String> getAddedKPIs();
  
  public void setddedKPIs(List<String> addedKPIs);
  
  public List<String> getDeletedKPIs();
  
  public void setDeletedKPIs(List<String> deletedKPIs);
  
  public List<String> getAddedUsers();
  
  public void setAddedUsers(List<String> endpointIdList);
  
  public List<String> getDeletedUsers();
  
  public void setDeletedUsers(List<String> endpointIdList);
  
  public List<String> getAddedEndpoints();
  
  public void setAddedEndpoints(List<String> endpointIdList);
  
  public List<String> getDeletedEndpoints();
  
  public void setDeletedEndpoints(List<String> endpointIdList);
  
  public List<String> getAddedTargetKlasses();
  
  public void setAddedTargetKlasses(List<String> tarhetKlasses);
  
  public List<String> getDeletedTargetKlasses();
  
  public void setDeletedTargetKlasses(List<String> tarhetKlasses);
  
  public List<String> getAddedTargetTaxonomies();
  
  public void setAddedTargetTaxonomies(List<String> availableTaxonomies);
  
  public List<String> getDeletedTargetTaxonomies();
  
  public void setDeletedTargetTaxonomies(List<String> targetTaxonomies);
  
  public List<String> getDeletedSystemIds();
  
  public void setDeletedSystemIds(List<String> deletedSystemIds);
  
  public List<String> getAddedSystemIds();
  
  public void setaddedSystemIds(List<String> addedSystemIds);
}
