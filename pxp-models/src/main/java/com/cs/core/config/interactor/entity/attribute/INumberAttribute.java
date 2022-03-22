package com.cs.core.config.interactor.entity.attribute;

public interface INumberAttribute extends IAttribute {
  
  public static final String PRECISION = "precision";
  public static final String HIDE_SEPARATOR = "hideSeparator";
  
  public Integer getPrecision();
  
  public void setPrecision(Integer precision);
  
  public Boolean getHideSeparator();
  
  public void setHideSeparator(Boolean hideSeparator);
}
