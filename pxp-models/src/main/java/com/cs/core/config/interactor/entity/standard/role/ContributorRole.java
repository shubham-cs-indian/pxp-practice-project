package com.cs.core.config.interactor.entity.standard.role;

public class ContributorRole extends AbstractMandatoryRole implements IContributorRole {
  
  private static final long serialVersionUID = 1L;
  
  public ContributorRole()
  {
  }
  
  public ContributorRole(String label)
  {
    this.label = label;
  }
}
