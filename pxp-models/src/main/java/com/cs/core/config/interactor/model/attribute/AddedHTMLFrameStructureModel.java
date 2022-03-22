package com.cs.core.config.interactor.model.attribute;

import com.cs.core.config.interactor.entity.structure.IStructureValidator;
import com.cs.core.config.interactor.entity.visualattribute.HTMLFrameStructure;
import com.cs.core.config.interactor.entity.visualattribute.HTMLFrameStructureValidator;
import com.cs.core.config.interactor.entity.visualattribute.IHTMLFrameStructure;
import com.cs.core.config.interactor.model.configdetails.AbstractAddedStructureModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class AddedHTMLFrameStructureModel extends AbstractAddedStructureModel
    implements IHTMLFrameStructure {
  
  private static final long serialVersionUID = 1L;
  
  public AddedHTMLFrameStructureModel()
  {
    super(new HTMLFrameStructure());
  }
  
  public AddedHTMLFrameStructureModel(HTMLFrameStructure htmlFrameStructure)
  {
    super(htmlFrameStructure);
  }
  
  @JsonDeserialize(as = HTMLFrameStructureValidator.class)
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
