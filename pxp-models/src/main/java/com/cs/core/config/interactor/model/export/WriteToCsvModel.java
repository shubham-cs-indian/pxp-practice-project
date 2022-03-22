package com.cs.core.config.interactor.model.export;

import java.util.ArrayList;
import java.util.List;

public class WriteToCsvModel implements IWriteToCsvModel {
  
  private static final long serialVersionUID = 1L;
  protected String          filePath;
  protected String          fileName;
  protected String[]        headers;
  protected List<String[]>  data;
  
  @Override
  public String getFilePath()
  {
    return filePath;
  }
  
  @Override
  public void setFilePath(String filePath)
  {
    this.filePath = filePath;
  }
  
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
  
  @Override
  public String[] getHeaders()
  {
    return headers;
  }
  
  @Override
  public void setHeaders(String[] headers)
  {
    this.headers = headers;
  }
  
  @Override
  public List<String[]> getData()
  {
    if (data == null) {
      data = new ArrayList<>();
    }
    return data;
  }
  
  @Override
  public void setData(List<String[]> data)
  {
    this.data = data;
  }
}
