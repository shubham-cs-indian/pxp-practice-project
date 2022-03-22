package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IModifiedTargetFiltersModel extends IModel {
  
  public static final String ADDED_KLASS_IDS      = "addedKlassIds";
  public static final String DELETED_KLASS_IDS    = "deletedKlassIds";
  public static final String ADDED_TAXONOMY_IDS   = "addedTaxonomyIds";
  public static final String DELETED_TAXONOMY_IDS = "deletedTaxonomyIds";
  
  public List<String> getAddedKlassIds();
  
  public void setAddedKlassIds(List<String> addedKlassIds);
  
  public List<String> getDeletedKlassIds();
  
  public void setDeletedKlassIds(List<String> deletedKlassIds);
  
  public List<String> getAddedTaxonomyIds();
  
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds);
  
  public List<String> getDeletedTaxonomyIds();
  
  public void setDeletedTaxonomyIds(List<String> deletedTaxonomyIds);
}
