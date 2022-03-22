package com.cs.core.config.interactor.entity.attribute;

public abstract class AbstractUnitAttribute extends AbstractAttribute implements IUnitAttribute {
  
  private static final long serialVersionUID = 1L;
  
  protected String          defaultUnit;
  
  protected Integer         precision;
  
  protected Boolean         hideSeparator    = false;  
  
  @Override
  public String getDefaultUnit()
  {
    return defaultUnit;
  }
  
  @Override
  public void setDefaultUnit(String defaultUnit)
  {
    this.defaultUnit = defaultUnit;
  }
  
  @Override
  public Integer getPrecision()
  {
    return precision;
  }
  
  @Override
  public void setPrecision(Integer precision)
  {
    this.precision = precision;
  }
  
  @Override
  public Boolean getHideSeparator()
  {
    return this.hideSeparator;
  }
  
  @Override
  public void setHideSeparator(Boolean hideSeparator)
  {
    this.hideSeparator = hideSeparator;
  }
  
}
