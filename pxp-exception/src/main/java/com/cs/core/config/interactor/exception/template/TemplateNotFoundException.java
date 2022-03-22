package com.cs.core.config.interactor.exception.template;

import com.cs.core.exception.NotFoundException;

public class TemplateNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public TemplateNotFoundException()
  {
  }
  
  public TemplateNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
