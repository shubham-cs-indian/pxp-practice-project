package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICreateVariantForLimitedObjectRequestModel extends IModel {
  
  public static final String CREATE_VARIANT_REQUEST = "createVariantRequest";
  public static final String TABLE_VIEW_REQUEST     = "tableViewRequest";
  
  public ICreateVariantModel getCreateVariantRequest();
  
  public void setCreateVariantRequest(ICreateVariantModel createVariantRequest);
  
  public IGetVariantInstanceInTableViewRequestModel getTableViewRequest();
  
  public void setTableViewRequest(IGetVariantInstanceInTableViewRequestModel tableViewRequest);
}
