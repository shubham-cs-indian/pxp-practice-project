package com.cs.core.config.interactor.model.propertycollection;

import com.cs.core.config.interactor.model.grid.ConfigGetAllRequestModel;

public class GetAllPropertyCollectionRequestModel extends ConfigGetAllRequestModel
    implements IGetAllPropertyCollectionRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected Boolean         isForXRay;
  
  public GetAllPropertyCollectionRequestModel()
  {
  }
  
  public GetAllPropertyCollectionRequestModel(Boolean isForXRay)
  {
    this.isForXRay = isForXRay;
  }
  
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
