package com.cs.core.config.interactor.model.processdetails;

public class ProcessVariantStatusModel extends ProcessKlassInstanceStatusModel
    implements IProcessVariantStatusModel {
  
  private static final long serialVersionUID = 1L;
  protected String          klassInstanceId;
  protected String          parentId;
  
  @Override
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  @Override
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
}
