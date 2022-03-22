package com.cs.core.runtime.interactor.model.instancetree;

public class GetBookmarkRequestModel extends GetInstanceTreeRequestModel
    implements IGetBookmarkRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          clickedTaxonomyId;
  
  @Override
  public String getClickedTaxonomyId()
  {
    return clickedTaxonomyId;
  }
  
  @Override
  public void setClickedTaxonomyId(String clickedTaxonomyId)
  {
    this.clickedTaxonomyId = clickedTaxonomyId;
  }
  
}
