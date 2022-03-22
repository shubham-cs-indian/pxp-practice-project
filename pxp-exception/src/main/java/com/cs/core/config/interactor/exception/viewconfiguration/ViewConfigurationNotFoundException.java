package com.cs.core.config.interactor.exception.viewconfiguration;

import com.cs.core.exception.NotFoundException;

public class ViewConfigurationNotFoundException  extends NotFoundException {
	  
	  private static final long serialVersionUID = 1L;
	  
	  public ViewConfigurationNotFoundException()
	  {
	    super();
	  }
	  
	  public ViewConfigurationNotFoundException(NotFoundException e)
	  {
	    super(e);
	  }
	}