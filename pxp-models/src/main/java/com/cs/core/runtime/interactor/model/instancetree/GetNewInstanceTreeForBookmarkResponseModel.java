package com.cs.core.runtime.interactor.model.instancetree;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetNewInstanceTreeForBookmarkResponseModel extends GetNewInstanceTreeResponseModel
    implements IGetNewInstanceTreeForBookmarkResponseModel {
  
  private static final long          serialVersionUID = 1L;
  protected IGetBookmarkRequestModel getRequestModel;
  
  @Override
  public IGetBookmarkRequestModel getGetRequestModel()
  {
    return getRequestModel;
  }
  
  @Override
  @JsonDeserialize(as = GetBookmarkRequestModel.class)
  public void setGetRequestModel(IGetBookmarkRequestModel getRequestModel)
  {
    this.getRequestModel = getRequestModel;
  }
  
}
