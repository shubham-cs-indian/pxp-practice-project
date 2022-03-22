package com.cs.core.config.interactor.entity.standard.role;

public class ApproverRole extends AbstractMandatoryRole implements IApproverRole {
  
  private static final long serialVersionUID = 1L;
  
  public ApproverRole()
  {
  }
  
  public ApproverRole(String label)
  {
    this.label = label;
  }
}
