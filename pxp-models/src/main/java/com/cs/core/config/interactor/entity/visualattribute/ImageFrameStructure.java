package com.cs.core.config.interactor.entity.visualattribute;

import com.cs.core.config.interactor.entity.structure.AbstractStructure;
import com.cs.core.config.interactor.entity.structure.IStructureValidator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ImageFrameStructure extends AbstractStructure implements IImageFrameStructure {
  
  private static final long              serialVersionUID = 1L;
  protected ImageFrameStructureValidator validator;
  
  @Override
  public IStructureValidator getValidator()
  {
    return this.validator;
  }
  
  @JsonDeserialize(as = ImageFrameStructureValidator.class)
  @Override
  public void setValidator(IStructureValidator validator)
  {
    this.validator = (ImageFrameStructureValidator) validator;
  }
}
