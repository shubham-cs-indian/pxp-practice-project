package com.cs.core.runtime.interactor.model.fileinstance;

import java.util.List;

import com.cs.core.config.interactor.model.permission.FunctionPermissionModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetFileNameListResponseModel implements IGetFileNameListResponseModel {
  
  private static final long          serialVersionUID = 1L;
  protected List<String>             fileNamesList;
  protected Long                     from             = 0l;
  protected Integer                  count            = 0;
  protected IFunctionPermissionModel functionPermission;
  
  @Override
  public List<String> getFileNamesList()
  {
    return fileNamesList;
  }
  
  @Override
  public void setFileNamesList(List<String> fileNamesList)
  {
    this.fileNamesList = fileNamesList;
  }
  
  @Override
  public Long getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Long from)
  {
    this.from = from;
  }
  
  @Override
  public Integer getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Integer count)
  {
    this.count = count;
  }
  
  @Override
  public IFunctionPermissionModel getFunctionPermission()
  {
    return functionPermission;
  }
  
  @Override
  @JsonDeserialize(as = FunctionPermissionModel.class)
  public void setFunctionPermission(IFunctionPermissionModel functionPermission)
  {
    this.functionPermission = functionPermission;
  }
}
