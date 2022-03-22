package com.cs.core.runtime.interactor.model.typeswitch;

public class GetAllowedTypesModel implements IGetAllowedTypesModel {
  
  protected String id;
  protected String standardKlassId;
  protected String mode;
  
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
  public String getStandardKlassId()
  {
    return standardKlassId;
  }
  
  @Override
  public void setStandardKlassId(String standardKlassId)
  {
    this.standardKlassId = standardKlassId;
  }
  
  @Override
  public String getMode()
  {
    return mode;
  }
  
  @Override
  public void setMode(String mode)
  {
    this.mode = mode;
  }
}
