package com.cs.core.config.interactor.model.organization;

import java.util.List;

public interface ISaveOrganizationModel extends IOrganizationModel {
  
  public static final String DELETED_TAXONOMY_IDS = "deletedTaxonomyIds";
  public static final String ADDED_TAXONOMY_IDS   = "addedTaxonomyIds";
  
  public static final String DELETED_KLASS_IDS    = "deletedKlassIds";
  public static final String ADDED_KLASS_IDS      = "addedKlassIds";
  
  public static final String DELETED_ENDPOINT_IDS = "deletedEndpointIds";
  public static final String ADDED_ENDPOINT_IDS   = "addedEndpointIds";
  
  public static final String DELETED_SYSTEM_IDS   = "deletedSystemIds";
  public static final String ADDED_SYSTEM_IDS     = "addedSystemIds";
  
  public List<String> getDeletedTaxonomyIds();
  
  public void setDeletedTaxonomyIds(List<String> deletedTaxonomyIds);
  
  public List<String> getAddedTaxonomyIds();
  
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds);
  
  public List<String> getDeletedKlassIds();
  
  public void setDeletedKlassIds(List<String> deletedKlassIds);
  
  public List<String> getAddedKlassIds();
  
  public void setAddedKlassIds(List<String> addedKlassIds);
  
  public List<String> getDeletedEndpointIds();
  
  public void setDeletedEndpointIds(List<String> deletedEndpointIds);
  
  public List<String> getAddedEndpointIds();
  
  public void getAddedEndpointIds(List<String> addedEndpointIds);
  
  public List<String> getDeletedSystemIds();
  
  public void setDeletedSystemIds(List<String> deletedSystemIds);
  
  public List<String> getAddedSystemIds();
  
  public void setaddedSystemIds(List<String> addedSystemIds);
}
