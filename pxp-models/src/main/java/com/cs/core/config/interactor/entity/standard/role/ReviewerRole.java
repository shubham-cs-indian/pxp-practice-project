package com.cs.core.config.interactor.entity.standard.role;

public class ReviewerRole extends AbstractMandatoryRole implements IReviewerRole {
  
  private static final long serialVersionUID = 1L;
  
  public ReviewerRole()
  {
  }
  
  public ReviewerRole(String label)
  {
    this.label = label;
  }
}
