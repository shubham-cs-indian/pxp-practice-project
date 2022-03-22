package com.cs.core.config.interactor.entity.attribute;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true)
@JsonSubTypes({ @Type(value = ConcatenatedAttributeOperator.class, name = "attribute"),
    @Type(value = ConcatenatedTagOperator.class, name = "tag"),
    @Type(value = ConcatenatedHtmlOperator.class, name = "html") })
public abstract class AbstractConcatenatedOperator implements IConcatenatedOperator {
  
  private static final long serialVersionUID = 1L;
  
  protected String          type;
  protected Integer         order;
  protected String          id;
  protected String          code;
  
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
  public Integer getOrder()
  {
    return order;
  }
  
  @Override
  public void setOrder(Integer order)
  {
    this.order = order;
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
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
  
  @Override
  @JsonIgnore
  public Long getVersionId()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionId(Long versionId)
  {
  }
  
  @Override
  @JsonIgnore
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @Override
  @JsonIgnore
  public String getLastModifiedBy()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
}
