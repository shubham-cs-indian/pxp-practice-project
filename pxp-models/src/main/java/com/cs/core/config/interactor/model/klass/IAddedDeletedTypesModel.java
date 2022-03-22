package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IAddedDeletedTypesModel extends IModel {
  
  public static final String ADDED_KLASS_IDS       = "addedKlassIds";
  public static final String ADDED_TAXONOMY_IDS    = "addedTaxonomyIds";
  public static final String REFERENCED_TAXONOMIES = "referencedTaxonomies";
  
  public List<String> getAddedKlassIds();
  
  public void setAddedKlassIds(List<String> addedKlassIds);
  
  public List<String> getAddedTaxonomyIds();
  
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds);
  
  public Map<String, Object> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(Map<String, Object> referencedTaxonomies);
}
