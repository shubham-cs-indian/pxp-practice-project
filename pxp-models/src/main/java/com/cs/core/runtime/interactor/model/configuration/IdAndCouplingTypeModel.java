package com.cs.core.runtime.interactor.model.configuration;

public class IdAndCouplingTypeModel implements IIdAndCouplingTypeModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          couplingType;
  
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
  public String getCouplingType()
  {
    return couplingType;
  }
  
  @Override
  public void setCouplingType(String couplingType)
  {
    this.couplingType = couplingType;
  }
}
