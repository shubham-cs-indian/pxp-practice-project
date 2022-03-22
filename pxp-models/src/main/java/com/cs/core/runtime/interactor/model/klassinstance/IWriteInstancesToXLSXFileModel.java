package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.component.IWriteInstancesToFileModel;

public interface IWriteInstancesToXLSXFileModel extends IWriteInstancesToFileModel {
  
  public static final String SHEET_NAME                  = "sheetName";
  public static final String FILE_INSTANCE_ID_FOR_EXPORT = "fileInstanceIdForExport";
  
  public String getSheetName();
  
  public void setSheetName(String sheetName);
  
  public String getFileInstanceIdForExport();
  
  public void setFileInstanceIdForExport(String fileInstanceIdForExport);
}
