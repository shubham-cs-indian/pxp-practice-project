package com.cs.core.runtime.interactor.model.dataintegration;

import java.util.List;

import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDataIntegrationInfoReturnModel extends IModel{
  
  public static final String DATA_INTEGRATION_INFO = "dataIntegrationInfo";
  public static final String FUNCTION_PERMISSION   = "functionPermission";
  
  public List<IDataIntegrationInfoModel> getDataIntegrationInfo();
  
  public void setDataIntegrationInfo(List<IDataIntegrationInfoModel> dataIntegrationInfo);
  
  public IFunctionPermissionModel getFunctionPermission();
  
  public void setFunctionPermission(IFunctionPermissionModel functionPermission);
}
