package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Collection;
import java.util.List;

public interface ITypesTaxonomiesModel extends IModel {
  
  String TYPES        = "types";
  String TAXONOMY_IDS = "taxonomyIds";
  
  public Collection<String> getTypes();
  
  public void setTypes(Collection<String> types);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
}
