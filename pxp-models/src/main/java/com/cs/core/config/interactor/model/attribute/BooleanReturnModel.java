package com.cs.core.config.interactor.model.attribute;

public class BooleanReturnModel implements IBooleanReturnModel {
  
  protected boolean duplicateStatus;
  
  @Override
  public boolean getDuplicateStatus()
  {
    return duplicateStatus;
  }
  
  @Override
  public void setDuplicateStatus(boolean duplicateStatus)
  {
    this.duplicateStatus = duplicateStatus;
  }
  
}
