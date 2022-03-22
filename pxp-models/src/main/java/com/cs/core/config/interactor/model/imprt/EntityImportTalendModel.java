package com.cs.core.config.interactor.model.imprt;

import com.cs.core.runtime.interactor.model.offboarding.CustomExportResponseModel;

public class EntityImportTalendModel extends CustomExportResponseModel
    implements IEntityImportTalendModel {
  
  private static final long serialVersionUID = 1L;
  protected String          fileName;
  
  @Override
  public String getFileName()
  {
    return fileName;
  }
  
  @Override
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
}
