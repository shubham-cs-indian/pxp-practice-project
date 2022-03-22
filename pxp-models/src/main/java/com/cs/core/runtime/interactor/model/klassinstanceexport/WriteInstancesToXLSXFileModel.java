package com.cs.core.runtime.interactor.model.klassinstanceexport;

import com.cs.core.runtime.interactor.model.fileexport.WriteInstancesToFileModel;
import com.cs.core.runtime.interactor.model.klassinstance.IWriteInstancesToXLSXFileModel;

public class WriteInstancesToXLSXFileModel extends WriteInstancesToFileModel
    implements IWriteInstancesToXLSXFileModel {
  
  private static final long serialVersionUID = 1L;
  protected String          fileInstanceIdForExport;
  String                    sheetName;
  
  @Override
  public String getSheetName()
  {
    return this.sheetName;
  }
  
  @Override
  public void setSheetName(String sheetName)
  {
    this.sheetName = sheetName;
  }
  
  @Override
  public String getFileInstanceIdForExport()
  {
    return fileInstanceIdForExport;
  }
  
  @Override
  public void setFileInstanceIdForExport(String fileInstanceIdForExport)
  {
    this.fileInstanceIdForExport = fileInstanceIdForExport;
  }
}
