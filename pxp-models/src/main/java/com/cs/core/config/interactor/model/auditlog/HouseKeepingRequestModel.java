package com.cs.core.config.interactor.model.auditlog;

public class HouseKeepingRequestModel implements IHouseKeepingRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected int             offset           = 0;
  
  @Override
  public int getOffset()
  {
    return offset;
  }
  
  @Override
  public void setOffset(int offset)
  {
    this.offset = offset;
  }
}
