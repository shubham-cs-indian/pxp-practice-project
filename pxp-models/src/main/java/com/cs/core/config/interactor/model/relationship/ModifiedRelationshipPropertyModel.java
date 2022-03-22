package com.cs.core.config.interactor.model.relationship;


public class ModifiedRelationshipPropertyModel implements IModifiedRelationshipPropertyModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          side;
  protected String          id;
  protected String          couplingType     = "looselyCoupled";
  
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
  
  @Override
  public String getSide()
  {
    return side;
  }
  
  @Override
  public void setSide(String side)
  {
    this.side = side;
  }
}
