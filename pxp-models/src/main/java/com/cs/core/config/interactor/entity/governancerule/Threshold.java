package com.cs.core.config.interactor.entity.governancerule;

public class Threshold implements IThreshold {
  
  private static final long serialVersionUID = 1L;
  protected Integer         lower;
  protected Integer         upper;
  
  public Integer getLower()
  {
    return lower;
  }
  
  public void setLower(Integer lower)
  {
    this.lower = lower;
  }
  
  public Integer getUpper()
  {
    return upper;
  }
  
  public void setUpper(Integer upper)
  {
    this.upper = upper;
  }
  
  @Override
  public String getId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setId(String id)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
}
