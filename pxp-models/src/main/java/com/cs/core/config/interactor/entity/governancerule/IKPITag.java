package com.cs.core.config.interactor.entity.governancerule;

import java.util.List;

public interface IKPITag {
  
  public static final String TAG_ID     = "tagId";
  public static final String TAG_VALUES = "tagValues";
  
  public String getTagId();
  
  public void setTagId(String tagId);
  
  public List<String> getTagValues();
  
  public void setTagValues(List<String> tagValues);
}
