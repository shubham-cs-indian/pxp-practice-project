package com.cs.core.config.interactor.model.attribute;

import com.cs.core.config.interactor.entity.structure.IStructureValidator;
import com.cs.core.config.interactor.entity.visualattribute.IImageVisualAttributeFrameStructure;
import com.cs.core.config.interactor.entity.visualattribute.ImageVisualAttributeFrameStructure;
import com.cs.core.config.interactor.entity.visualattribute.ImageVisualAttributeFrameStructureValidator;
import com.cs.core.config.interactor.model.configdetails.AbstractAddedVisualAttributeStructureModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class AddedImageVisualAttributeFrameStructureModel extends
    AbstractAddedVisualAttributeStructureModel implements IImageVisualAttributeFrameStructure {
  
  private static final long serialVersionUID = 1L;
  
  public AddedImageVisualAttributeFrameStructureModel()
  {
    super(new ImageVisualAttributeFrameStructure());
  }
  
  public AddedImageVisualAttributeFrameStructureModel(
      ImageVisualAttributeFrameStructure imageVisualAttributeFrameStructure)
  {
    super(imageVisualAttributeFrameStructure);
  }
  
  @JsonDeserialize(as = ImageVisualAttributeFrameStructureValidator.class)
  @Override
  public void setValidator(IStructureValidator validator)
  {
    entity.setValidator(validator);
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
  
  @Override
  public String getVariantOf()
  {
    return ((IImageVisualAttributeFrameStructure) entity).getVariantOf();
  }
  
  @Override
  public void setVariantOf(String variantOf)
  {
    ((IImageVisualAttributeFrameStructure) entity).setVariantOf(variantOf);
  }
}
