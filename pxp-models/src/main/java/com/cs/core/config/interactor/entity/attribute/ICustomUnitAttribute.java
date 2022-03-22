package com.cs.core.config.interactor.entity.attribute;

public interface ICustomUnitAttribute extends IUnitAttribute {
  
  public static final String DEFAULT_UNIT_AS_HTML = "defaultUnitAsHTML";
  
  public String getDefaultUnitAsHTML();
  
  public void setDefaultUnitAsHTML(String defaultUnitAsHTML);
}
