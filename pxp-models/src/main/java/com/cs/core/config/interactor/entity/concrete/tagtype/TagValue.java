package com.cs.core.config.interactor.entity.concrete.tagtype;

import com.cs.core.config.interactor.entity.tag.ITagValue;

public class TagValue implements ITagValue {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  
  protected Long            versionId;
  
  protected Long            versionTimestamp;
  
  protected String          label;
  
  protected String          color;
  
  protected Integer         relevance;
  
  protected String          lastModifiedBy;
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
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public Long getVersionId()
  {
    return this.versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return this.versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public String getLabel()
  {
    return this.label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getColor()
  {
    return this.color;
  }
  
  @Override
  public void setColor(String color)
  {
    this.color = color;
  }
  
  @Override
  public Integer getRelevance()
  {
    return this.relevance;
  }
  
  @Override
  public void setRelevance(Integer relevance)
  {
    this.relevance = relevance;
  }
}
