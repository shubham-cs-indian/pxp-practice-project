package com.cs.core.config.interactor.entity.condition;

public abstract class AbstractConditionEntity implements IConditionEntity {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          type;
  protected String          description;
  protected String          color;
  protected String          listId;
  protected String          lastModifiedBy;
  protected Long            versionId;
  protected Long            versionTimestamp;
  protected String          baseType         = this.getClass()
      .getName();
  
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
  
  @Override
  public String getDescription()
  {
    return description;
  }
  
  @Override
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  @Override
  public String getColor()
  {
    return color;
  }
  
  @Override
  public void setColor(String color)
  {
    this.color = color;
  }
  
  @Override
  public String getListId()
  {
    return listId;
  }
  
  @Override
  public void setListId(String listId)
  {
    this.listId = listId;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
}
