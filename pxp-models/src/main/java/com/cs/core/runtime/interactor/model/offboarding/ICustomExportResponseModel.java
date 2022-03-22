package com.cs.core.runtime.interactor.model.offboarding;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICustomExportResponseModel extends IModel {
  
  public static String FILE_STREAM = "fileStream";
  
  public byte[] getFileStream();
  
  public void setFileStream(byte[] fileStream);
}
