package com.cs.core.config.interactor.entity.attribute;

public interface IUnitAttribute extends IAttribute {
  
  public static final String DEFAULT_UNIT = "defaultUnit";
  
  public static final String PRECISION    = "precision";
  
  public static final String HIDE_SEPARATOR = "hideSeparator";
  
  public String getDefaultUnit();
  
  public void setDefaultUnit(String defaultUnit);
  
  public Integer getPrecision();
  
  public void setPrecision(Integer Precision);
  
  public Boolean getHideSeparator();
  
  public void setHideSeparator(Boolean hideSeparator);
  
}
