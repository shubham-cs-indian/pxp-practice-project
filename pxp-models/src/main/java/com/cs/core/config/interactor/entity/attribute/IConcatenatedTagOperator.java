package com.cs.core.config.interactor.entity.attribute;

public interface IConcatenatedTagOperator extends IConcatenatedOperator {
  
  public static final String TAG_ID = "tagId";
  
  public String getTagId();
  
  public void setTagId(String tagId);
}
