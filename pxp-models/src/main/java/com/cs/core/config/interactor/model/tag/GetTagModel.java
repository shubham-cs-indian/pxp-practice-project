package com.cs.core.config.interactor.model.tag;

public class GetTagModel implements IGetTagModel {
  
  protected String id;
  protected String mode;
  
  public GetTagModel(String id, String mode)
  {
    super();
    this.id = id;
    this.mode = mode;
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
