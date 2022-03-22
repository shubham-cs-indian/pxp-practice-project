package com.cs.core.runtime.strategy.model.couplingtype;

public class IdCodeCouplingTypeModel implements IIdCodeCouplingTypeModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          code;
  protected String          id;
  protected String          couplingType;
  
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
