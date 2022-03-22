package com.cs.core.config.interactor.entity.visualattribute;

import java.util.ArrayList;
import java.util.List;

public class HTMLVisualAttributeFrameStructureValidator extends HTMLFrameStructureValidator
    implements IHTMLVisualAttributeStructureValidator {
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    defaultStyles    = new ArrayList<>();
  
  protected Boolean         shouldVersion;
  
  @Override
  public List<String> getDefaultStyles()
  {
    return this.defaultStyles;
  }
  
  @Override
  public void setDefaultStyles(List<String> defaultStyles)
  {
    this.defaultStyles = defaultStyles;
  }
  
  @Override
  public Boolean getShouldVersion()
  {
    return shouldVersion;
  }
  
  @Override
  public void setShouldVersion(Boolean shouldVersion)
  {
    this.shouldVersion = shouldVersion;
  }
}
