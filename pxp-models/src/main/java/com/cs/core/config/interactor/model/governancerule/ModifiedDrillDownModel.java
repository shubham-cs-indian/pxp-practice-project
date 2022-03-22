package com.cs.core.config.interactor.model.governancerule;

import java.util.List;

public class ModifiedDrillDownModel implements IModifiedDrillDownModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          type;
  protected List<String>    addedTypes;
  protected List<String>    deletedTypes;
  
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
  public List<String> getAddedTypes()
  {
    return addedTypes;
  }
  
  @Override
  public void setAddedTypes(List<String> addedTypes)
  {
    this.addedTypes = addedTypes;
  }
  
  @Override
  public List<String> getDeletedTypes()
  {
    return deletedTypes;
  }
  
  @Override
  public void setDeletedTypes(List<String> deletedTypes)
  {
    this.deletedTypes = deletedTypes;
  }
}
