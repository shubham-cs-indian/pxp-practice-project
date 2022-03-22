package com.cs.runtime.interactor.model.downloadtracker;

import com.cs.core.runtime.interactor.model.configuration.IModel;


public interface IExportDownloadLogResponseModel extends IModel {
  
  public static final String CSV_BYTES         = "csvBytes";
  
  public byte[] getCsvBytes();
  public void setCsvBytes(byte[] csvBytes);
}
