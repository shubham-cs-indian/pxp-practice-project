package com.cs.core.runtime.interactor.model.dataintegration;

import java.util.List;

import com.cs.core.config.interactor.model.permission.FunctionPermissionModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.runtime.interactor.model.dataintegration.DataIntegrationInfoModel;
import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationInfoModel;
import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationInfoReturnModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DataIntegrationInfoReturnModel implements IDataIntegrationInfoReturnModel {
  
  private static final long                 serialVersionUID = 1L;
  protected List<IDataIntegrationInfoModel> dataIntegrationInfo;
  protected IFunctionPermissionModel        functionPermission;
  
  @Override
  public List<IDataIntegrationInfoModel> getDataIntegrationInfo()
  {
    return dataIntegrationInfo;
  }
  
  @Override
  @JsonDeserialize(contentAs = DataIntegrationInfoModel.class)
  public void setDataIntegrationInfo(List<IDataIntegrationInfoModel> dataIntegrationInfo)
  {
    this.dataIntegrationInfo = dataIntegrationInfo;
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

