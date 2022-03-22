package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.List;

public interface IKlassInstancesForComparisonStrategyModel
    extends IKlassInstancesListForComparisonModel {
  
  public static String TYPES        = "types";
  public static String TAXONOMY_IDS = "taxonomyIds";
  
  public List<String> getTypes();
  
  public void setTypes(List<String> types);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
}
