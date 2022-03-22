package com.cs.core.config.interactor.entity.visualattribute;

import com.cs.core.config.interactor.entity.structure.AbstractStructure;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.config.interactor.entity.structure.IStructureValidator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class HTMLFrameStructure extends AbstractStructure implements IHTMLFrameStructure {
  
  private static final long             serialVersionUID  = 1L;
  
  protected String                      id;
  
  protected String                      label;
  
  protected String                      icon;
  
  protected HTMLFrameStructureValidator validator;
  
  protected List<IStructure>            structureChildren = new ArrayList<>();
  
  @Override
  public IStructureValidator getValidator()
  {
    return this.validator;
  }
  
  @JsonDeserialize(as = HTMLFrameStructureValidator.class)
  @Override
  public void setValidator(IStructureValidator validator)
  {
    this.validator = (HTMLFrameStructureValidator) validator;
  }
}
