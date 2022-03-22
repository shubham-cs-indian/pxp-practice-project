package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IParentChildTaxonomyIdModel extends IModel {
  
  public static final String TAXONOMY_MAP = "taxonomyMap";
  
  public Map<String, List<String>> getTaxonomyMap();
  
  public void setTaxonomyMap(Map<String, List<String>> taxonomyMap);
}
