package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForVersionRollbackModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForVersionRollbackModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class RestoreVariantRequestModel implements IRestoreVariantRequestModel {
  
  private static final long                          serialVersionUID = 1L;
  
  protected String                                   id;
  protected IGetConfigDetailsForVersionRollbackModel configDetails;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public IGetConfigDetailsForVersionRollbackModel getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDetailsForVersionRollbackModel.class)
  public void setConfigDetails(IGetConfigDetailsForVersionRollbackModel configDetails)
  {
    this.configDetails = configDetails;
  }
}
