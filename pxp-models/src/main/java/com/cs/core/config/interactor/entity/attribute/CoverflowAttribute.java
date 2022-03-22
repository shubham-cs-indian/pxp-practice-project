package com.cs.core.config.interactor.entity.attribute;

public class CoverflowAttribute extends AbstractAttribute implements ICoverflowAttribute {
  
  private static final long serialVersionUID     = 1L;
  
  protected Integer         numberOfItemsAllowed = 0;
  
  @Override
  public Integer getNumberOfItemsAllowed()
  {
    return numberOfItemsAllowed;
  }
  
  @Override
  public void setNumberOfItemsAllowed(Integer numberOfItemsAllowed)
  {
    this.numberOfItemsAllowed = numberOfItemsAllowed;
  }
  
  @Override
  public String getRendererType()
  {
    return Renderer.COVERFLOW.name();
  }
}
