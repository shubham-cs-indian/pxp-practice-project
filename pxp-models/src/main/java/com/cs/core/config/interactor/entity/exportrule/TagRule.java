package com.cs.core.config.interactor.entity.exportrule;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class TagRule implements ITagRule {
  
  protected String              tagId;
  protected List<ITagValueRule> tagValues;
  protected String              operator;
  
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
  public List<ITagValueRule> getTagValues()
  {
    return tagValues;
  }
  
  @JsonDeserialize(contentAs = TagValueRule.class)
  @Override
  public void setTagValues(List<ITagValueRule> tagValues)
  {
    this.tagValues = tagValues;
  }
  
  @Override
  public String getOperator()
  {
    return operator;
  }
  
  @Override
  public void setOperator(String operator)
  {
    this.operator = operator;
  }
}
