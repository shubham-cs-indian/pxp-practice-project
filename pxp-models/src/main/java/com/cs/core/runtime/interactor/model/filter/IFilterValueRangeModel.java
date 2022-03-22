package com.cs.core.runtime.interactor.model.filter;

public interface IFilterValueRangeModel extends IFilterValueModel {
  
  public Double getTo();
  
  public void setTo(Double to);
  
  public Double getFrom();
  
  public void setFrom(Double from);
}
