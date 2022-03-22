package com.cs.core.config.interactor.entity.standard.role;

public class OwnerRole extends AbstractMandatoryRole implements IOwnerRole {
  
  private static final long serialVersionUID = 1L;
  
  public OwnerRole()
  {
  }
  
  public OwnerRole(String label)
  {
    this.label = label;
  }
}
