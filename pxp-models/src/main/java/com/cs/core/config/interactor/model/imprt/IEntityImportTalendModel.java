package com.cs.core.config.interactor.model.imprt;

import com.cs.core.runtime.interactor.model.offboarding.ICustomExportResponseModel;

public interface IEntityImportTalendModel extends ICustomExportResponseModel {
  
  public static final String FILE_NAME = "fileName";
  
  public String getFileName();
  
  public void setFileName(String fileName);
}
