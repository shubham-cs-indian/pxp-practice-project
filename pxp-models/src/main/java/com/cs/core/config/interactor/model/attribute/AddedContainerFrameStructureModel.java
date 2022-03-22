package com.cs.core.config.interactor.model.attribute;

import com.cs.core.config.interactor.entity.structure.IStructureValidator;
import com.cs.core.config.interactor.entity.visualattribute.ContainerFrameStructure;
import com.cs.core.config.interactor.entity.visualattribute.ContainerFrameStructureValidator;
import com.cs.core.config.interactor.entity.visualattribute.IContainerFrameStructure;
import com.cs.core.config.interactor.model.configdetails.AbstractAddedStructureModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class AddedContainerFrameStructureModel extends AbstractAddedStructureModel
    implements IContainerFrameStructure {
  
  private static final long serialVersionUID = 1L;
  
  public AddedContainerFrameStructureModel()
  {
    super(new ContainerFrameStructure());
  }
  
  public AddedContainerFrameStructureModel(IContainerFrameStructure classFrameStructure)
  {
    super(classFrameStructure);
  }
  
  @JsonDeserialize(as = ContainerFrameStructureValidator.class)
  @Override
  public void setValidator(IStructureValidator validator)
  {
    super.setValidator(validator);
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
