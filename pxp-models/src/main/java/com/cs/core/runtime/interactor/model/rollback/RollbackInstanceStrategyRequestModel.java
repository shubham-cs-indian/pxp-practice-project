package com.cs.core.runtime.interactor.model.rollback;

import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForVersionRollbackModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForVersionRollbackModel;
import com.cs.core.runtime.interactor.model.versionrollback.IRollbackInstanceStrategyRequestModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class RollbackInstanceStrategyRequestModel implements IRollbackInstanceStrategyRequestModel {
  
  private static final long                          serialVersionUID = 1L;
  protected String                                   id;
  protected String                                   versionId;
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
  public String getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(String versionId)
  {
    this.versionId = versionId;
  }
  
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
