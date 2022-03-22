package com.cs.core.config.interactor.model.duplicatecode;

import com.cs.core.config.interactor.entity.datarule.IElementConflictingValuesModel;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(name = "attribute",
        value = AttributeConflictingValuesModel.class),
    @JsonSubTypes.Type(name = "tag",
        value = TagConflictingValuesModel.class),
    @JsonSubTypes.Type(name = "relationship",
        value = RelationshipConflictingValuesModel.class) })
public abstract class AbstractElementConflictingValuesModel
    implements IElementConflictingValuesModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          type;
  protected String          sourceType;
  
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
  
  public String getSourceType()
  {
    return sourceType;
  }
  
  public void setSourceType(String sourceType)
  {
    this.sourceType = sourceType;
  }
}
