package com.cs.core.config.interactor.entity.attribute;

public class CustomUnitAttribute extends AbstractUnitAttribute implements ICustomUnitAttribute {
  
  private static final long serialVersionUID = 1L;
  
  protected String          defaultUnitAsHTML;
  
  @Override
  public String getDefaultUnitAsHTML()
  {
    return defaultUnitAsHTML;
  }
  
  @Override
  public void setDefaultUnitAsHTML(String defaultUnitAsHTML)
  {
    this.defaultUnitAsHTML = defaultUnitAsHTML;
  }
  
  @Override
  public String getRendererType()
  {
    return Renderer.CUSTOM.name();
  }
}
