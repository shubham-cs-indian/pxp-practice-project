package com.cs.core.runtime.interactor.model.collections;

import java.util.ArrayList;
import java.util.List;

public class ConfigDetailsForBookmarkRequestModel implements IConfigDetailsForBookmarkRequestModel {
  
  private static final long serialVersionUID    = 1L;
  protected List<String>    selectedTaxonomyIds = new ArrayList<>();
  
  public List<String> getSelectedTaxonomyIds()
  {
    return selectedTaxonomyIds;
  }
  
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    this.selectedTaxonomyIds = selectedTaxonomyIds;
  }
}
