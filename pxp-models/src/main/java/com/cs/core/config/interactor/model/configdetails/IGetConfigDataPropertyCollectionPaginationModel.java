package com.cs.core.config.interactor.model.configdetails;

public interface IGetConfigDataPropertyCollectionPaginationModel
    extends IGetConfigDataEntityPaginationModel {
  
  public static final String IS_FOR_XRAY = "isForXRay";
  
  public Boolean getIsForXRay();
  
  public void setIsForXRay(Boolean isForXRay);
}
