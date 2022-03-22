package com.cs.core.config.interactor.entity.visualattribute;

import com.cs.core.config.interactor.entity.structure.AbstractStructure;
import com.cs.core.config.interactor.entity.structure.IStructureValidator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ClassFrameStructure extends AbstractStructure implements IClassFrameStructure {
  
  private static final long              serialVersionUID = 1L;
  protected ClassFrameStructureValidator validator;
  
  protected String                       referenceId;
  
  @Override
  public IStructureValidator getValidator()
  {
    return this.validator;
  }
  
  @JsonDeserialize(as = ClassFrameStructureValidator.class)
  @Override
  public void setValidator(IStructureValidator validator)
  {
    this.validator = (ClassFrameStructureValidator) validator;
  }
  
  @Override
  public String getReferenceId()
  {
    return this.referenceId;
  }
  
  @Override
  public void setReferenceId(String referenceId)
  {
    this.referenceId = referenceId;
  }
}
