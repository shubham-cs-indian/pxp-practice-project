package com.cs.core.config.interactor.exception.timelineevent;

import com.cs.core.exception.NotFoundException;

public class TimelineEventNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public TimelineEventNotFoundException()
  {
  }
  
  public TimelineEventNotFoundException(String message, String errorCode)
  {
    super();
  }
}
