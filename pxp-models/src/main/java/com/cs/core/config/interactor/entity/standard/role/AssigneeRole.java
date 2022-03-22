package com.cs.core.config.interactor.entity.standard.role;

public class AssigneeRole extends AbstractMandatoryRole implements IAssigneeRole {
  
  private static final long serialVersionUID = 1L;
  
  public AssigneeRole()
  {
  }
  
  public AssigneeRole(String label)
  {
    super(label);
  }
}
