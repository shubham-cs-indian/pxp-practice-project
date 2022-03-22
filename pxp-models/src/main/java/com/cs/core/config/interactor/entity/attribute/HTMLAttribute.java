package com.cs.core.config.interactor.entity.attribute;

import com.cs.core.config.interactor.entity.visualattribute.HTMLVisualAttributeFrameStructureValidator;
import com.cs.core.config.interactor.entity.visualattribute.IVisualAttributeValidator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class HTMLAttribute extends AbstractAttribute implements IHTMLAttribute {
  
  private static final long                            serialVersionUID = 1L;
  
  protected HTMLVisualAttributeFrameStructureValidator validator        = new HTMLVisualAttributeFrameStructureValidator();

  
  @Override
  public IVisualAttributeValidator getValidator()
  {
    return this.validator;
  }
  
  @JsonDeserialize(as = HTMLVisualAttributeFrameStructureValidator.class)
  @Override
  public void setValidator(IVisualAttributeValidator validator)
  {
    this.validator = (HTMLVisualAttributeFrameStructureValidator) validator;
  }
  
  @Override
  public String getRendererType()
  {
    return Renderer.HTML.name();
  }
}
