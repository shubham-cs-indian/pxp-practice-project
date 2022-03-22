package com.cs.core.config.interactor.model.tag;

import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.JsonProcessingException;

@JsonTypeInfo(use = Id.NONE)
public class TagModel extends AbstractTagModel implements ITagModel {
  
  private static final long serialVersionUID = 1L;
  
  public TagModel()
  {
    super(new Tag());
  }
  
  public TagModel(Tag tag)
  {
    super(tag);
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
