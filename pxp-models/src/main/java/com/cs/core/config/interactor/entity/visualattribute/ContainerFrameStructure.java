package com.cs.core.config.interactor.entity.visualattribute;

import com.cs.core.config.interactor.entity.structure.AbstractStructure;
import com.cs.core.config.interactor.entity.structure.IStructureValidator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ContainerFrameStructure extends AbstractStructure implements IContainerFrameStructure {
  
  private static final long                  serialVersionUID = 1L;
  protected ContainerFrameStructureValidator validator;
  
  @Override
  public IStructureValidator getValidator()
  {
    return this.validator;
  }
  
  @JsonDeserialize(as = ContainerFrameStructureValidator.class)
  @Override
  public void setValidator(IStructureValidator validator)
  {
    this.validator = (ContainerFrameStructureValidator) validator;
  }
}
