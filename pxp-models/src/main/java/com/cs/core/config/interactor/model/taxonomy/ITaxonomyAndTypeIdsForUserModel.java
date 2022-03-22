package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ITaxonomyAndTypeIdsForUserModel extends IModel {
  
  public static String TAXONOMY_IDS = "taxonomyIds";
  public static String TYPES        = "types";
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public List<String> getTypes();
  
  public void setTypes(List<String> types);
}
