package com.cs.core.config.interactor.entity.visualattribute;

import java.util.List;

public interface IHTMLVisualAttributeStructureValidator
    extends IVisualAttributeValidator, IHTMLFrameStructureValidator {
  
  public List<String> getDefaultStyles();
  
  public void setDefaultStyles(List<String> defaultStyles);
}
