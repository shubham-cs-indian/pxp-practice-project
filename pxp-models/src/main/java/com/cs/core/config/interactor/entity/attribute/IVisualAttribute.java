package com.cs.core.config.interactor.entity.attribute;

import com.cs.core.config.interactor.entity.visualattribute.IVisualAttributeValidator;

public interface IVisualAttribute extends IAttribute {
  
  public IVisualAttributeValidator getValidator();
  
  public void setValidator(IVisualAttributeValidator validator);
}
