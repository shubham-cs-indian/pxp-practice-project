package com.cs.core.config.interactor.model.component;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IWriteInstancesToFileModel extends IModel {
  
  public static final String FILE_PATH         = "filePath";
  public static final String FILE_NAME         = "fileName";
  public static final String HEADER_ROW_NUMBER = "headerRowNumber";
  public static final String DATA_ROW_NUMBER   = "dataRowNumber";
  public static final String HEADER_TO_WRITE   = "headerToWrite";
  public static final String DATA_TO_WRITE     = "dataToWrite";
  
  public String getfilePath();
  
  public void setfilePath(String filePath);
  
  public String getfileName();
  
  public void setfileName(String fileName);
  
  public int getHeaderRowNumber();
  
  public void setHeaderRowNumber(int headerRowNumber);
  
  public int getDataRowNumber();
  
  public void setDataRowNumber(int dataRowNumber);
  
  public String[] getHeaderToWrite();
  
  public void setHeaderToWrite(String[] headerToWrite);
  
  public List<String[]> getDataToWrite();
  
  public void setDataToWrite(List<String[]> dataToWrite);
}
