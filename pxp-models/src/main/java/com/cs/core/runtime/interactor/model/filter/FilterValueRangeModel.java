package com.cs.core.runtime.interactor.model.filter;

public class FilterValueRangeModel extends AbstractFilterValueModel
    implements IFilterValueRangeModel {
  
  protected Double to;
  
  protected Double from;
  
  @Override
  public Double getTo()
  {
    return this.to;
  }
  
  @Override
  public void setTo(Double to)
  {
    this.to = to;
  }
  
  @Override
  public Double getFrom()
  {
    return this.from;
  }
  
  @Override
  public void setFrom(Double from)
  {
    this.from = from;
  }
}
