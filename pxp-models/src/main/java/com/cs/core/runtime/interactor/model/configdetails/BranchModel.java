package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.config.interactor.model.configdetails.IBranchModel;

public class BranchModel implements IBranchModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          parent;
  
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
  public String getParent()
  {
    return parent;
  }
  
  @Override
  public void setParent(String parent)
  {
    this.parent = parent;
  }
}
