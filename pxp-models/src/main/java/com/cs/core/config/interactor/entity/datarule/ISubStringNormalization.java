package com.cs.core.config.interactor.entity.datarule;

public interface ISubStringNormalization extends INormalization {
  
  public static final String START_INDEX = "startIndex";
  public static final String END_INDEX   = "endIndex";
  
  public Integer getStartIndex();
  
  public void setStartIndex(Integer startIndex);
  
  public Integer getEndIndex();
  
  public void setEndIndex(Integer endIndex);
}
