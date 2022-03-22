package com.cs.core.config.interactor.model.articlemarketrelationship;

public class ContentDetailsModel implements IContentDetailsModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected Long            versionId;
  protected Integer         count;
  
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
  public Integer getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Integer count)
  {
    this.count = count;
  }
}
