package com.cs.core.config.interactor.model.configdetails;

public class GetConfigDataPropertyCollectionPaginationModel extends
    GetConfigDataEntityPaginationModel implements IGetConfigDataPropertyCollectionPaginationModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Boolean         isForXRay        = false;
  
  @Override
  public Boolean getIsForXRay()
  {
    return isForXRay;
  }
  
  @Override
  public void setIsForXRay(Boolean isForXRay)
  {
    this.isForXRay = isForXRay;
  }
}
