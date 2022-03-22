package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IImageAttribute;
import com.cs.core.config.interactor.entity.attribute.ImageAttribute;
import com.cs.core.config.interactor.entity.visualattribute.IVisualAttributeValidator;
import com.cs.core.config.interactor.entity.visualattribute.ImageVisualAttributeFrameStructureValidator;
import com.cs.core.config.interactor.model.configdetails.AbstractAttributeModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ImageAttributeModel extends AbstractAttributeModel implements IImageAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public ImageAttributeModel()
  {
    super(new ImageAttribute(), Renderer.IMAGE.toString());
  }
  
  public ImageAttributeModel(IImageAttribute attribute)
  {
    super(attribute, Renderer.IMAGE.toString());
  }
  
  @Override
  public IVisualAttributeValidator getValidator()
  {
    return ((IImageAttribute) attribute).getValidator();
  }
  
  @JsonDeserialize(as = ImageVisualAttributeFrameStructureValidator.class)
  @Override
  public void setValidator(IVisualAttributeValidator validator)
  {
    ((IImageAttribute) attribute).setValidator(validator);
  }
  
  @Override
  public String getRendererType()
  {
    return rendererType;
  }
  
  @Override
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
