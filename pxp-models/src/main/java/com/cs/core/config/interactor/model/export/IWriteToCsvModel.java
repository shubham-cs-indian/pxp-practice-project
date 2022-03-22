package com.cs.core.config.interactor.model.export;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IWriteToCsvModel extends IModel {
  
  public static String FILE_PATH = "filePath";
  public static String FILE_NAME = "fileName";
  public static String HEADERS   = "headers";
  public static String DATA      = "data";
  
  public String getFilePath();
  
  public void setFilePath(String filePath);
  
  public String getFileName();
  
  public void setFileName(String fileName);
  
  public String[] getHeaders();
  
  public void setHeaders(String[] headers);
  
  public List<String[]> getData();
  
  public void setData(List<String[]> data);
}
