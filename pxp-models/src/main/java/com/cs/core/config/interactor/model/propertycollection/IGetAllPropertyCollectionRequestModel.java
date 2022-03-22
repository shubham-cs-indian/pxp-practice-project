package com.cs.core.config.interactor.model.propertycollection;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

public interface IGetAllPropertyCollectionRequestModel extends IConfigGetAllRequestModel {
  
  public static final String IS_FOR_X_RAY = "isForXRay";
  
  public Boolean getIsForXRay();
  
  public void setIsForXRay(Boolean isForXRay);
}
