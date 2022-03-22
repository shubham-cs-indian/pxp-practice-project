package com.cs.core.config.interactor.entity.attribute;

public class ConcatenatedTagOperator extends AbstractConcatenatedOperator
    implements IConcatenatedTagOperator {
  
  private static final long serialVersionUID = 1L;
  
  protected String          tagId;
  
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
}
