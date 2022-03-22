package com.cs.core.runtime.interactor.model.configdetails;

import java.util.List;

public interface IConfigDetailsForBulkPropagationRequestModel
    extends IMulticlassificationRequestModel {
  
  public static final String ADDED_KLASS_IDS    = "addedKlassIds";
  public static final String ADDED_TAXONOMY_IDS = "addedTaxonomyIds";
  
  public List<String> getAddedKlassIds();
  
  public void setAddedKlassIds(List<String> addedKlassIds);
  
  public List<String> getAddedTaxonomyIds();
  
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds);
}
