package com.cs.core.runtime.interactor.model.dataintegration;

public class NomenclatureElementModel implements INomenclatureElementModel {
  
  private static final long serialVersionUID = 1L;
  
  public String             id, name, parentId;
  public int                level;
  
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
  public String getName()
  {
    return name;
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
  
  @Override
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
  
  @Override
  public int getLevel()
  {
    return this.level;
  }
  
  @Override
  public void setLevel(int level)
  {
    this.level = level;
  }
}
