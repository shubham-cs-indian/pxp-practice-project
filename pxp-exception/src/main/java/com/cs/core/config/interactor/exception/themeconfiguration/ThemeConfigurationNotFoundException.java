package com.cs.core.config.interactor.exception.themeconfiguration;

import com.cs.core.exception.NotFoundException;

public class ThemeConfigurationNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ThemeConfigurationNotFoundException()
  {
    super();
  }
  
  public ThemeConfigurationNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
