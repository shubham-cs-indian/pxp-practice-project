package com.cs.core.config.interactor.entity.visualattribute;

import com.cs.core.config.interactor.entity.structure.IStructureValidator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ImageVisualAttributeFrameStructure extends AbstractVisualAttributeStructure
    implements IImageVisualAttributeFrameStructure {
  
  private static final long                             serialVersionUID = 1L;
  
  protected String                                      id;
  
  protected ImageVisualAttributeFrameStructureValidator validator;
  
  protected String                                      referenceId;
  
  protected String                                      variantOf;
  
  @Override
  public IStructureValidator getValidator()
  {
    return this.validator;
  }
  
  @JsonDeserialize(as = ImageVisualAttributeFrameStructureValidator.class)
  @Override
  public void setValidator(IStructureValidator validator)
  {
    this.validator = (ImageVisualAttributeFrameStructureValidator) validator;
  }
  
  public String getReferenceId()
  {
    return referenceId;
  }
  
  public void setReferenceId(String referenceId)
  {
    this.referenceId = referenceId;
  }
  
  @Override
  public String getVariantOf()
  {
    return variantOf;
  }
  
  @Override
  public void setVariantOf(String variantOf)
  {
    this.variantOf = variantOf;
  }
}
