package com.cs.core.runtime.interactor.exception.configuration;

import com.cs.core.exception.NotFoundException;

public class FileNotFoundToDownloadException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public FileNotFoundToDownloadException()
  {
  }
  
  public FileNotFoundToDownloadException(FileNotFoundToDownloadException e)
  {
    super(e);
  }
}
