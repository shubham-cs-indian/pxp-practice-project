package com.cs.core.runtime.interactor.model.configdetails;

import java.util.List;

public interface IConfigDetailsForSwitchTypeRequestModel extends IMulticlassificationRequestModel {
  
  public static final String ADDED_KLASS_ID       = "addedKlassId";
  public static final String ADDED_TAXONOMY_ID    = "addedTaxonomyId";
  public static final String DELETED_TAXONOMY_ID  = "deletedTaxonomyId";
  public static final String ADDED_KLASS_IDS      = "addedKlassIds";
  public static final String ADDED_TAXONOMY_IDS   = "addedTaxonomyIds";
  public static final String DELETED_TAXONOMY_IDS = "deletedTaxonomyIds";
  public static final String TAXONOMY_IDS         = "taxonomyIds";
  
  public String getDeletedTaxonomyId();
  
  public void setDeletedTaxonomyId(String deletedTaxonomyId);
  
  public List<String> getAddedTaxonomyIds();
  
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds);
  
  public List<String> getDeletedTaxonomyIds();
  
  public void setDeletedTaxonomyIds(List<String> deletedTaxonomyIds);
  
  public List<String> getAddedKlassIds();
  
  public void setAddedKlassIds(List<String> addedKlassIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> addedTaxonomyIds);
}
