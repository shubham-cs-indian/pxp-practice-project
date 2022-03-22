package com.cs.runtime.interactor.model.downloadtracker;


public class ExportDownloadLogResponseModel implements IExportDownloadLogResponseModel {
  
  private static final long serialVersionUID = 1L;
  protected byte[]          csvBytes;
  
  @Override
  public byte[] getCsvBytes()
  {
    return csvBytes;
  }

  @Override
  public void setCsvBytes(byte[] csvBytes)
  {
    this.csvBytes = csvBytes;
  }
}
