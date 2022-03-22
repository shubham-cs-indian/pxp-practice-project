package com.cs.core.config.interactor.entity.standard.role;

public class SeniorManagerRole extends AbstractMandatoryRole implements ISeniorManagerRole {
  
  private static final long serialVersionUID = 1L;
  
  public SeniorManagerRole()
  {
  }
  
  public SeniorManagerRole(String label)
  {
    this.label = label;
  }
}
