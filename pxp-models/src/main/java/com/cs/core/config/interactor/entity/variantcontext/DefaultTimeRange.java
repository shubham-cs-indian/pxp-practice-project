package com.cs.core.config.interactor.entity.variantcontext;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DefaultTimeRange implements IDefaultTimeRange {
  
  private static final long serialVersionUID = 1L;
  
  protected Long            from;
  protected Long            to;
  protected Boolean         isCurrentTime;
  protected String          code;
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
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
  public Boolean getIsCurrentTime()
  {
    return isCurrentTime;
  }
  
  @Override
  public void setIsCurrentTime(Boolean isCurrentTime)
  {
    this.isCurrentTime = isCurrentTime;
  }
  
  @JsonIgnore
  @Override
  public String getId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setId(String id)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
}
