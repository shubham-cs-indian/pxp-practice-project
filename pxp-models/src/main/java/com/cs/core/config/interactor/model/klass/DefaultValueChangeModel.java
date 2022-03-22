package com.cs.core.config.interactor.model.klass;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true)
@JsonSubTypes({
    @Type(value = AttributeDefaultValueCouplingTypeModel.class, name = "attribute"),
    @Type(value = TagDefaultValueCouplingTypeModel.class, name = "tag"),
    @Type(value = DefaultValueChangeModel.class, name = "taxonomy") })
public class DefaultValueChangeModel extends DefaultValueCouplingTypeModel
    implements IDefaultValueChangeModel {
  
  private static final long serialVersionUID    = 1L;
  
  protected String          entityId;
  protected List<String>    klassAndChildrenIds = new ArrayList<>();
  protected String          type;
  protected String          sourceType;
  protected long            propertyIID;

  
  @Override
  public String getEntityId()
  {
    return entityId;
  }
  
  @Override
  public void setEntityId(String entityId)
  {
    this.entityId = entityId;
  }
  
  @Override
  public List<String> getKlassAndChildrenIds()
  {
    return klassAndChildrenIds;
  }
  
  @Override
  public void setKlassAndChildrenIds(List<String> klassAndChildrenIds)
  {
    this.klassAndChildrenIds = klassAndChildrenIds;
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
  
  @Override
  public String getSourceType()
  {
    return sourceType;
  }
  
  @Override
  public void setSourceType(String sourceType)
  {
    this.sourceType = sourceType;
  }
  
  @Override
  public long getPropertyIID()
  {
    return propertyIID;
  }
  
  @Override
  public void setPropertyIID(long propertyIID)
  {
    this.propertyIID = propertyIID;
  }
}
