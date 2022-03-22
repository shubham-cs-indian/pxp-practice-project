package com.cs.core.config.interactor.model.tag;

import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.template.IModifiedSequenceModel;
import com.cs.core.config.interactor.model.template.ModifiedSequenceModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = Id.NONE)
public class SaveTagModel extends Tag implements ISaveTagModel {
  
  private static final long        serialVersionUID = 1L;
  
  protected IModifiedSequenceModel modifiedSequence;
  
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
  public IModifiedSequenceModel getModifiedSequence()
  {
    return modifiedSequence;
  }
  
  @Override
  @JsonDeserialize(as = ModifiedSequenceModel.class)
  public void setModifiedSequence(IModifiedSequenceModel modifiedSequence)
  {
    this.modifiedSequence = modifiedSequence;
  }
}
