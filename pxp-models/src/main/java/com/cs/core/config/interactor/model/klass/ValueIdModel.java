package com.cs.core.config.interactor.model.klass;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = ValueIdAttributeModel.class, name = "attribute"),
    @Type(value = ValueIdTagModel.class, name = "tag") })
public class ValueIdModel implements IValueIdModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          type;
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
}
