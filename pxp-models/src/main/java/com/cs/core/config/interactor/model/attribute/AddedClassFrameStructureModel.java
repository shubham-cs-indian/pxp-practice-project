package com.cs.core.config.interactor.model.attribute;

import com.cs.core.config.interactor.entity.structure.IStructureValidator;
import com.cs.core.config.interactor.entity.visualattribute.ClassFrameStructure;
import com.cs.core.config.interactor.entity.visualattribute.ClassFrameStructureValidator;
import com.cs.core.config.interactor.entity.visualattribute.IClassFrameStructure;
import com.cs.core.config.interactor.model.configdetails.AbstractAddedStructureModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class AddedClassFrameStructureModel extends AbstractAddedStructureModel
    implements IClassFrameStructure {
  
  private static final long serialVersionUID = 1L;
  
  public AddedClassFrameStructureModel()
  {
    super(new ClassFrameStructure());
  }
  
  public AddedClassFrameStructureModel(IClassFrameStructure classFrameStructure)
  {
    super(classFrameStructure);
  }
  
  @Override
  public String getReferenceId()
  {
    return ((IClassFrameStructure) this.entity).getReferenceId();
  }
  
  @Override
  public void setReferenceId(String referenceId)
  {
    ((IClassFrameStructure) this.entity).setReferenceId(referenceId);
  }
  
  @JsonDeserialize(as = ClassFrameStructureValidator.class)
  @Override
  public void setValidator(IStructureValidator validator)
  {
    entity.setValidator(validator);
  }
  
  @Override
  public Boolean getIsGhost()
  {
    return entity.getIsGhost();
  }
  
  @Override
  public void setIsGhost(Boolean isGhost)
  {
    entity.setIsGhost(isGhost);
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
