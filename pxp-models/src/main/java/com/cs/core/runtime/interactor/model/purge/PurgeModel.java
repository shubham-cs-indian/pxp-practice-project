package com.cs.core.runtime.interactor.model.purge;

public class PurgeModel implements IPurgeModel {
  
  private static final long serialVersionUID = 1L;
  protected Double          purgeProcessTime;
  protected Long            purgeTime;
  
  @Override
  public Long getPurgeTime()
  {
    return purgeTime;
  }
  
  @Override
  public void setPurgeTime(Long purgeTime)
  {
    this.purgeTime = purgeTime;
  }
  
  @Override
  public Double getPurgeProcessTime()
  {
    return purgeProcessTime;
  }
  
  @Override
  public void setPurgeProcessTime(Double purgeProcessTime)
  {
    this.purgeProcessTime = purgeProcessTime;
  }
}
