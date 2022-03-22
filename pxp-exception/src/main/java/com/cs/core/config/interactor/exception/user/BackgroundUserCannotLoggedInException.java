package com.cs.core.config.interactor.exception.user;

import com.cs.core.exception.NotFoundException;
import com.cs.core.exception.PluginException;

public class BackgroundUserCannotLoggedInException  extends  PluginException{
  
  private static final long serialVersionUID = 1L;
  
  public BackgroundUserCannotLoggedInException()
  {
    super();
  }
  
  public BackgroundUserCannotLoggedInException(NotFoundException e)
  {
    super(e);
  }
  
}
