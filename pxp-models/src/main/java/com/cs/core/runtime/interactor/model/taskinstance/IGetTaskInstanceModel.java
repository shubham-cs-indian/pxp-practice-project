package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.runtime.interactor.entity.taskinstance.ITaskInstance;
import com.cs.core.runtime.interactor.model.camunda.ICamundaProcessModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import java.util.Map;

@JsonTypeInfo(use = Id.NONE)
public interface IGetTaskInstanceModel extends ITaskInstance, IModel {
  
  public static final String REFERENCED_ASSETS             = "referencedAssets";
  public static final String GLOBAL_PERMISSION             = "globalPermission";
  public static final String REFERENCED_PROCESS_DEFINATION = "referencedProcessDefination";
  public static final String REFERENCED_INSTANCES          = "referencedInstances";
  public static final String CONFIG_DETAILS                = "configDetails";
  
  public Map<String, IAssetInfoModel> getReferencedAssets();
  
  public void setReferencedAssets(Map<String, IAssetInfoModel> referencedAssets);
  
  public IGlobalPermission getGlobalPermission();
  
  public void setGlobalPermission(IGlobalPermission globalPermission);
  
  public ICamundaProcessModel getReferencedProcessDefination();
  
  public void setReferencedProcessDefination(ICamundaProcessModel referencedProcessDefination);
  
  public Map<String, ITaskReferencedInstanceModel> getReferencedInstances();
  
  public void setReferencedInstances(Map<String, ITaskReferencedInstanceModel> referencedInstances);
  
  public IConfigTaskContentTypeModel getconfigDetails();
  
  public void setconfigDetails(IConfigTaskContentTypeModel configDetails);
}
