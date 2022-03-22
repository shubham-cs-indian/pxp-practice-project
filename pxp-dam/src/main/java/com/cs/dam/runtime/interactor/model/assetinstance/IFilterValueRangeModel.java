package com.cs.dam.runtime.interactor.model.assetinstance;

public interface IFilterValueRangeModel extends IFilterValueModel {
  
  public static final String FROM = "from";
  public static final String TO   = "to";
  
  public Double getTo();
  
  public void setTo(Double to);
  
  public Double getFrom();
  
  public void setFrom(Double from);
}
