package com.cs.core.config.interactor.model.taxonomy;

import java.util.List;

public class GetChildTaxonomiesByParentIdAndSelectedTaxonomiesModel
    implements IGetChildTaxonomiesByParentIdAndSelectedTaxonomiesRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    parentTaxonomies;
  protected List<String>    selectedTaxonomies;
  
  @Override
  public List<String> getParentTaxonomies()
  {
    return parentTaxonomies;
  }
  
  @Override
  public void setParentTaxonomies(List<String> parentTaxonomies)
  {
    this.parentTaxonomies = parentTaxonomies;
  }
  
  @Override
  public List<String> getSelectedTaxonomies()
  {
    return selectedTaxonomies;
  }
  
  @Override
  public void setSelectedTaxonomies(List<String> selectedTaxonomies)
  {
    this.selectedTaxonomies = selectedTaxonomies;
  }
}
