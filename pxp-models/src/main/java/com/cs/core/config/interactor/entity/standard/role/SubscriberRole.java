package com.cs.core.config.interactor.entity.standard.role;

public class SubscriberRole extends AbstractMandatoryRole implements ISubscriberRole {
  
  private static final long serialVersionUID = 1L;
  
  public SubscriberRole()
  {
  }
  
  public SubscriberRole(String label)
  {
    this.label = label;
  }
}
