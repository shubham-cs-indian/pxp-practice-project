package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetChildTaxonomiesByParentIdAndSelectedTaxonomiesRequestModel extends IModel {
  
  public static final String PARENT_TAXONOMIES   = "parentTaxonomies";
  public static final String SELECTED_TAXONOMIES = "selectedTaxonomies";
  
  public List<String> getParentTaxonomies();
  
  public void setParentTaxonomies(List<String> parentTaxonomies);
  
  public List<String> getSelectedTaxonomies();
  
  public void setSelectedTaxonomies(List<String> selectedTaxonomies);
}
