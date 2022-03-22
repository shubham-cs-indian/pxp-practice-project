package com.cs.core.runtime.interactor.model.taxonomy;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ITaxonomySearchStrategyModel extends IModel {
  
  public static final String PARENT_TAXONOMY_ID = "parentTaxonomyId";
  public static final String SEARCH_TEXT        = "searchText";
  
  public String getParentTaxonomyId();
  
  public void setParentTaxonomyId(String parentTaxonomyId);
  
  public String getSearchText();
  
  public void setSearchText(String searchText);
}
