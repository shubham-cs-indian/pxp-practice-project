package com.cs.core.config.interactor.entity.exportrule;

public class TagValueRule implements ITagValueRule {
  
  protected String innerTagId;
  protected Long   to;
  protected Long   from;
  
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
