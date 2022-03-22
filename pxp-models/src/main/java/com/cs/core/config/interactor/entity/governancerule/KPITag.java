package com.cs.core.config.interactor.entity.governancerule;

import java.util.List;

public class KPITag implements IKPITag {
  
  protected String       tagId;
  protected List<String> tagValues;
  
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
  public List<String> getTagValues()
  {
    return tagValues;
  }
  
  @Override
  public void setTagValues(List<String> tagValues)
  {
    this.tagValues = tagValues;
  }
}
