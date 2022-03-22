package com.cs.core.config.interactor.model.propertycollection;

public class AddedTabModel implements IAddedTabModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          label;
  protected String          code;
  protected Boolean         isNewlyCreated   = false;
  
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
  public Boolean getIsNewlyCreated()
  {
    return isNewlyCreated;
  }
  
  @Override
  public void setIsNewlyCreated(Boolean isNewlyCreated)
  {
    this.isNewlyCreated = isNewlyCreated;
  }
}
