package com.cs.core.runtime.interactor.model.offboarding;

public class CustomExportResponseModel implements ICustomExportResponseModel {
  
  private static final long serialVersionUID = 1L;
  protected byte[]          fileStream;
  
  public byte[] getFileStream()
  {
    return fileStream;
  }
  
  public void setFileStream(byte[] fileStream)
  {
    this.fileStream = fileStream;
  }
}
