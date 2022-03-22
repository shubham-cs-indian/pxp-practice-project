package com.cs.core.config.interactor.entity.exportrule;

import java.util.List;

public interface ITagRule {
  
  public String getTagId();
  
  public void setTagId(String tagId);
  
  public List<ITagValueRule> getTagValues();
  
  public void setTagValues(List<ITagValueRule> tagValues);
  
  public String getOperator();
  
  public void setOperator(String operator);
}
