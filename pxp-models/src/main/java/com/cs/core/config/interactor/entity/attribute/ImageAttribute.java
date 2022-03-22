package com.cs.core.config.interactor.entity.attribute;

import com.cs.core.config.interactor.entity.visualattribute.IVisualAttributeValidator;
import com.cs.core.config.interactor.entity.visualattribute.ImageVisualAttributeFrameStructureValidator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ImageAttribute extends AbstractAttribute implements IImageAttribute {
  
  private static final long                             serialVersionUID = 1L;
  
  protected ImageVisualAttributeFrameStructureValidator validator;
  
  @Override
  public IVisualAttributeValidator getValidator()
  {
    return this.validator;
  }
  
  @JsonDeserialize(as = ImageVisualAttributeFrameStructureValidator.class)
  @Override
  public void setValidator(IVisualAttributeValidator validator)
  {
    this.validator = (ImageVisualAttributeFrameStructureValidator) validator;
  }
  
  @Override
  public String getRendererType()
  {
    return Renderer.IMAGE.name();
  }
}
