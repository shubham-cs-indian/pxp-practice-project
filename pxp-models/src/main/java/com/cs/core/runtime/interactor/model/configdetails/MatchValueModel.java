package com.cs.core.runtime.interactor.model.configdetails;

public class MatchValueModel implements IMatchValueModel {
  
  protected String value;
  protected String percent;
  protected String count;
  
  @Override
  public String getValue()
  {
    return value;
  }
  
  @Override
  public void setValue(String value)
  {
    this.value = value;
  }
  
  @Override
  public String getPercent()
  {
    return percent;
  }
  
  @Override
  public void setPercent(String percent)
  {
    this.percent = percent;
  }
  
  @Override
  public String getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(String count)
  {
    this.count = count;
  }
}
