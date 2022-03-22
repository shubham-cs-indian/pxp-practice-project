package com.cs.core.config.interactor.model.endpoint;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.permission.FunctionPermissionModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetEndpointsInfoModel implements IGetEndointsInfoModel {
  
  private static final long                     serialVersionUID = 1L;
  
  protected List<IEndpointBasicInfoModel> endpoints        = new ArrayList<>();
  protected Long                                totalCount;
  protected String                              mode;
  protected IFunctionPermissionModel            functionPermission;

  @Override
  public List<IEndpointBasicInfoModel> getEndpoints()
  {
    return endpoints;
  }
  
  @Override
  @JsonDeserialize(contentAs = EndpointBasicInfoModel.class)
  public void setEndpoints(List<IEndpointBasicInfoModel> endpoints)
  {
    this.endpoints = endpoints;
  }
  
  @Override
  public Long getTotalCount()
  {
    return totalCount;
  }
  
  @Override
  public void setTotalCount(Long totalCount)
  {
    this.totalCount = totalCount;
  }
  
  @Override
  public String getMode()
  {
    return mode;
  }
  
  @Override
  public void setMode(String mode)
  {
    this.mode = mode;
  }
  
  @Override
  public IFunctionPermissionModel getFunctionPermisson()
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
