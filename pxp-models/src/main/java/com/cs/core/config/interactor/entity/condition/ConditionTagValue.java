package com.cs.core.config.interactor.entity.condition;

public class ConditionTagValue implements IConditionTagValue {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected Integer         from;
  protected Integer         to;
  protected String          lastModifiedBy;
  protected Long            versionId;
  protected Long            versionTimestamp;
  
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
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public Integer getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Integer from)
  {
    this.from = from;
  }
  
  @Override
  public Integer getTo()
  {
    return to;
  }
  
  @Override
  public void setTo(Integer to)
  {
    this.to = to;
  }
}
