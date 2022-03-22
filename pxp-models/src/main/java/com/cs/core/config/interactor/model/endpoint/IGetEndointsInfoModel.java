package com.cs.core.config.interactor.model.endpoint;

import java.util.List;

import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetEndointsInfoModel extends IModel {
  
  public static final String ENDPOINTS           = "endpoints";
  public static final String TOTAL_COUNT         = "totalCount";
  public static final String MODE                = "mode";
  public static final String FUNCTION_PERMISSION = "functionPermission";
  
  public List<IEndpointBasicInfoModel> getEndpoints();
  
  public void setEndpoints(List<IEndpointBasicInfoModel> endpointsList);
  
  public Long getTotalCount();
  
  public void setTotalCount(Long totalCount);
  
  public String getMode();
  
  public void setMode(String mode);
  
  public IFunctionPermissionModel getFunctionPermisson();
  
  public void setFunctionPermission(IFunctionPermissionModel functionPermission);
}
