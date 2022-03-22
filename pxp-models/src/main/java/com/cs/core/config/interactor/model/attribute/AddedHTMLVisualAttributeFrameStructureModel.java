package com.cs.core.config.interactor.model.attribute;

import com.cs.core.config.interactor.entity.structure.IStructureValidator;
import com.cs.core.config.interactor.entity.visualattribute.HTMLVisualAttributeFrameStructure;
import com.cs.core.config.interactor.entity.visualattribute.HTMLVisualAttributeFrameStructureValidator;
import com.cs.core.config.interactor.entity.visualattribute.IHTMLVisualAttributeFrameStructure;
import com.cs.core.config.interactor.model.configdetails.AbstractAddedVisualAttributeStructureModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class AddedHTMLVisualAttributeFrameStructureModel extends
    AbstractAddedVisualAttributeStructureModel implements IHTMLVisualAttributeFrameStructure {
  
  private static final long serialVersionUID = 1L;
  
  public AddedHTMLVisualAttributeFrameStructureModel()
  {
    super(new HTMLVisualAttributeFrameStructure());
  }
  
  public AddedHTMLVisualAttributeFrameStructureModel(
      HTMLVisualAttributeFrameStructure htmlVisualAttributeFrameStructure)
  {
    super(htmlVisualAttributeFrameStructure);
  }
  
  @JsonDeserialize(as = HTMLVisualAttributeFrameStructureValidator.class)
  @Override
  public void setValidator(IStructureValidator validator)
  {
    entity.setValidator(validator);
  }
  
  @Override
  public String getComment()
  {
    return ((IHTMLVisualAttributeFrameStructure) entity).getComment();
  }
  
  @Override
  public void setComment(String comment)
  {
    ((IHTMLVisualAttributeFrameStructure) entity).setComment(comment);
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
    return ((IHTMLVisualAttributeFrameStructure) entity).getVariantOf();
  }
  
  @Override
  public void setVariantOf(String variantOf)
  {
    ((IHTMLVisualAttributeFrameStructure) entity).setVariantOf(variantOf);
  }
}
