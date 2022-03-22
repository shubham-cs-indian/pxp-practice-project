package com.cs.core.runtime.interactor.model.instancetree;

import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;

public interface IGetInstanceTreeForBookmarkRequestModel extends IGetInstanceTreeRequestModel {
  
  public static final String BOOKMARK_ID = "bookmarkId";
  
  public String getBookmarkId();
  public void setBookmarkId(String bookmarkId);
}
