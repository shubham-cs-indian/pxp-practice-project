package com.cs.core.runtime.interactor.model.taxonomy;

public class TaxonomySearchStrategyModel implements ITaxonomySearchStrategyModel {
  
  private static final long serialVersionUID = 1L;
  protected String          searchText;
  protected String          parentTaxonomyId;
  
  public TaxonomySearchStrategyModel(String searchText, String parentTaxonomyId)
  {
    this.searchText = searchText;
    this.parentTaxonomyId = parentTaxonomyId;
  }
  
  @Override
  public String getSearchText()
  {
    return searchText;
  }
  
  @Override
  public void setSearchText(String searchText)
  {
    this.searchText = searchText;
  }
  
  @Override
  public String getParentTaxonomyId()
  {
    return parentTaxonomyId;
  }
  
  @Override
  public void setParentTaxonomyId(String parentTaxonomyId)
  {
    this.parentTaxonomyId = parentTaxonomyId;
  }
}
