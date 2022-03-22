package com.cs.core.config.interactor.exception.organization;

import com.cs.core.exception.NotFoundException;

public class OrganizationNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public OrganizationNotFoundException()
  {
    super();
  }
  
  public OrganizationNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
