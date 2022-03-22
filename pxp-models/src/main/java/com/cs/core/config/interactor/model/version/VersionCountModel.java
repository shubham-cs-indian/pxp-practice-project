package com.cs.core.config.interactor.model.version;

public class VersionCountModel implements IVersionCountModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Integer         maxVersionCount  = 0;
  
  @Override
  public Integer getMaxVersionCount()
  {
    return maxVersionCount;
  }
  
  @Override
  public void setMaxVersionCount(Integer maxVersionCount)
  {
    this.maxVersionCount = maxVersionCount;
  }
}
