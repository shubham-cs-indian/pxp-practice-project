package com.cs.core.runtime.interactor.entity.notification;

public class EntityInfo implements IEntityInfo {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          type;
  protected String          subEntityId;
  protected String          label;
  protected String          subEntityLabel;
  
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
  public String getSubEntityId()
  {
    return subEntityId;
  }
  
  @Override
  public void setSubEntityId(String subEntityId)
  {
    this.subEntityId = subEntityId;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getSubEntityLabel()
  {
    return subEntityLabel;
  }
  
  @Override
  public void setSubEntityLabel(String subEntityLabel)
  {
    this.subEntityLabel = subEntityLabel;
  }
}
