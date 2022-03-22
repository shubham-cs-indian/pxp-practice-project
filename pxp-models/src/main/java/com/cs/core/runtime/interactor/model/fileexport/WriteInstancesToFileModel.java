package com.cs.core.runtime.interactor.model.fileexport;

import com.cs.core.config.interactor.model.component.IWriteInstancesToFileModel;

import java.util.List;

public class WriteInstancesToFileModel implements IWriteInstancesToFileModel {
  
  private static final long serialVersionUID = 1L;
  String                    filePath;
  String                    fileName;
  int                       headerRowNumber;
  int                       dataRowNumber;
  String[]                  headerToWrite;
  List<String[]>            dataToWrite;
  
  @Override
  public String getfilePath()
  {
    return this.filePath;
  }
  
  @Override
  public void setfilePath(String filePath)
  {
    this.filePath = filePath;
  }
  
  @Override
  public String getfileName()
  {
    return this.fileName;
  }
  
  @Override
  public void setfileName(String fileName)
  {
    this.fileName = fileName;
  }
  
  @Override
  public int getHeaderRowNumber()
  {
    return this.headerRowNumber;
  }
  
  @Override
  public void setHeaderRowNumber(int headerRowNumber)
  {
    this.headerRowNumber = headerRowNumber;
  }
  
  @Override
  public int getDataRowNumber()
  {
    return this.dataRowNumber;
  }
  
  @Override
  public void setDataRowNumber(int dataRowNumber)
  {
    this.dataRowNumber = dataRowNumber;
  }
  
  @Override
  public String[] getHeaderToWrite()
  {
    return this.headerToWrite;
  }
  
  @Override
  public void setHeaderToWrite(String[] headerToWrite)
  {
    this.headerToWrite = headerToWrite;
  }
  
  @Override
  public List<String[]> getDataToWrite()
  {
    return this.dataToWrite;
  }
  
  @Override
  public void setDataToWrite(List<String[]> dataToWrite)
  {
    this.dataToWrite = dataToWrite;
  }
}
