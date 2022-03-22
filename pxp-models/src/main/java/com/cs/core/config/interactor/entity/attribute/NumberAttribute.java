package com.cs.core.config.interactor.entity.attribute;

public class NumberAttribute extends AbstractAttribute implements INumberAttribute {
  
  private static final long serialVersionUID = 1L;
  
  protected Integer         precision;
  
  protected Boolean         hideSeparator = false;
  
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
  public String getRendererType()
  {
    return Renderer.NUMBER.name();
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
