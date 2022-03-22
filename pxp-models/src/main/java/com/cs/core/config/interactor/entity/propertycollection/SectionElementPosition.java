package com.cs.core.config.interactor.entity.propertycollection;

public class SectionElementPosition implements IPosition {
  
  private static final long serialVersionUID = 1L;
  
  protected Integer         x;
  
  protected Integer         y;
  
  protected Long            versionId;
  
  protected Long            versionTimestamp;
  
  protected String          lastModifiedBy;
  
  protected String          code;
  
  public SectionElementPosition()
  {
  }
  
  public SectionElementPosition(Integer x, Integer y)
  {
    this.x = x;
    this.y = y;
  }
  
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
  public Integer getX()
  {
    return this.x;
  }
  
  @Override
  public void setX(Integer x)
  {
    this.x = x;
  }
  
  @Override
  public Integer getY()
  {
    return this.y;
  }
  
  @Override
  public void setY(Integer y)
  {
    this.y = y;
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
}
