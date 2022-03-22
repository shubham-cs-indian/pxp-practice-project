package com.cs.core.runtime.interactor.model.instancetree;


public interface IGetNewInstanceTreeForBookmarkResponseModel
    extends IGetNewInstanceTreeResponseModel {
  
  public static final String GET_REQUEST_MODEL = "getRequestModel";
  
  public IGetBookmarkRequestModel getGetRequestModel();
  public void setGetRequestModel(IGetBookmarkRequestModel getRequestModel);
}
