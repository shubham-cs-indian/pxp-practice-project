package com.cs.core.config.interactor.model.attribute;

import com.cs.core.config.interactor.entity.structure.IStructureValidator;
import com.cs.core.config.interactor.entity.visualattribute.IImageFrameStructure;
import com.cs.core.config.interactor.entity.visualattribute.ImageFrameStructure;
import com.cs.core.config.interactor.entity.visualattribute.ImageFrameStructureValidator;
import com.cs.core.config.interactor.model.configdetails.AbstractAddedStructureModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class AddedImageFrameStructureModel extends AbstractAddedStructureModel
    implements IImageFrameStructure {
  
  private static final long serialVersionUID = 1L;
  
  public AddedImageFrameStructureModel()
  {
    super(new ImageFrameStructure());
  }
  
  public AddedImageFrameStructureModel(ImageFrameStructure imageFrameStructure)
  {
    super(imageFrameStructure);
  }
  
  @JsonDeserialize(as = ImageFrameStructureValidator.class)
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
}
