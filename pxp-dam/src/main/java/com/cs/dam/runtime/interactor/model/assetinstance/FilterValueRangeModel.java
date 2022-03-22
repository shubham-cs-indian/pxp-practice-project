package com.cs.dam.runtime.interactor.model.assetinstance;

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
