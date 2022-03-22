package com.cs.core.config.interactor.entity.datarule;

public class DataRuleTagValues implements IDataRuleTagValues {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected Long            versionId;
  protected Long            versionTimestamp;
  protected String          lastModifiedBy;
  protected String          innerTagId;
  protected Long            to;
  protected Long            from;
  
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
  public String getInnerTagId()
  {
    return innerTagId;
  }
  
  @Override
  public void setInnerTagId(String innerTagId)
  {
    this.innerTagId = innerTagId;
  }
  
  @Override
  public Long getTo()
  {
    return to;
  }
  
  @Override
  public void setTo(Long to)
  {
    this.to = to;
  }
  
  @Override
  public Long getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Long from)
  {
    this.from = from;
  }
}
