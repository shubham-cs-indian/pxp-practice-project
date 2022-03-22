package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IConfigDetailsForBookmarkRequestModel extends IModel {
  
  public static final String SELECTED_TAXONOMYIDS = "selectedTaxonomyIds";
  
  public List<String> getSelectedTaxonomyIds();
  
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds);
}
