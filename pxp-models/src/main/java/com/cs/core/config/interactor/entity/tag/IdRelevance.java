package com.cs.core.config.interactor.entity.tag;

public class IdRelevance implements IIdRelevance {
  
  private static final long serialVersionUID = 1L;
  
  protected Integer         relevance;
  protected String          tagId;
  protected String          id;
  protected long            iID;
  protected String          code;
  
  @Override
  public String getTagId()
  {
    return tagId;
  }
  
  @Override
  public void setTagId(String tagId)
  {
    this.tagId = tagId;
  }
  
  @Override
  public Integer getRelevance()
  {
    return relevance;
  }
  
  @Override
  public void setRelevance(Integer relevance)
  {
    this.relevance = relevance;
  }
  
  @Override
  public Long getVersionId()
  {
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
  
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
  public long getiID()
  {
    return iID;
  }
  
  @Override
  public void setiID(long iID)
  {
    this.iID = iID;
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String tagCode)
  {
    this.code = tagCode;
  }
}
