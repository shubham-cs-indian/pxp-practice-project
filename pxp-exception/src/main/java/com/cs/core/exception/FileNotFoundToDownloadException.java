package com.cs.core.exception;

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
