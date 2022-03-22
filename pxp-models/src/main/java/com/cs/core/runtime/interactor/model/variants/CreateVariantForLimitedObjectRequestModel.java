package com.cs.core.runtime.interactor.model.variants;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CreateVariantForLimitedObjectRequestModel
    implements ICreateVariantForLimitedObjectRequestModel {
  
  private static final long                            serialVersionUID = 1L;
  
  protected ICreateVariantModel                        createVariantRequest;
  protected IGetVariantInstanceInTableViewRequestModel tableViewRequest;
  
  @JsonDeserialize(as = CreateVariantModel.class)
  @Override
  public ICreateVariantModel getCreateVariantRequest()
  {
    return createVariantRequest;
  }
  
  @Override
  public void setCreateVariantRequest(ICreateVariantModel createVariantRequest)
  {
    this.createVariantRequest = createVariantRequest;
  }
  
  @Override
  public IGetVariantInstanceInTableViewRequestModel getTableViewRequest()
  {
    return tableViewRequest;
  }
  
  @JsonDeserialize(as = GetVariantInstancesInTableViewRequestModel.class)
  @Override
  public void setTableViewRequest(IGetVariantInstanceInTableViewRequestModel tableViewRequest)
  {
    this.tableViewRequest = tableViewRequest;
  }
}
