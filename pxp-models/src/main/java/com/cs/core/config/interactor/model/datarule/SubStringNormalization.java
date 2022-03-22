package com.cs.core.config.interactor.model.datarule;

import com.cs.core.config.interactor.entity.datarule.ISubStringNormalization;

public class SubStringNormalization extends Normalization implements ISubStringNormalization {
  
  private static final long serialVersionUID = 1L;
  protected Integer         startIndex;
  protected Integer         endIndex;
  
  @Override
  public Integer getStartIndex()
  {
    return startIndex;
  }
  
  @Override
  public void setStartIndex(Integer startIndex)
  {
    this.startIndex = startIndex;
  }
  
  @Override
  public Integer getEndIndex()
  {
    return endIndex;
  }
  
  @Override
  public void setEndIndex(Integer endIndex)
  {
    this.endIndex = endIndex;
  }
}
