package com.cs.core.runtime.interactor.model.instancetree;


public class GetInstanceTreeForBookmarkRequestModel extends GetInstanceTreeRequestModel
    implements IGetInstanceTreeForBookmarkRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          bookmarkId;

  @Override
  public String getBookmarkId()
  {
    return bookmarkId;
  }
  
  @Override
  public void setBookmarkId(String bookmarkId)
  {
    this.bookmarkId = bookmarkId;
  }
  
}
