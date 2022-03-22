package com.cs.core.runtime.interactor.model.fileinstance;

import java.util.List;

import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetFileNameListResponseModel extends IModel {
  
  public static final String FILE_NAMES_LIST     = "fileNamesList";
  public static final String COUNT               = "count";
  public static final String FROM                = "from";
  public static final String FUNCTION_PERMISSION = "functionPermission";
  
  public List<String> getFileNamesList();  
  public void setFileNamesList(List<String> fileNamesList);
  
  public Integer getCount();  
  public void setCount(Integer count);
  
  public Long getFrom();  
  public void setFrom(Long from);
  
  public IFunctionPermissionModel getFunctionPermission();
  public void setFunctionPermission(IFunctionPermissionModel functionPermission);
}
