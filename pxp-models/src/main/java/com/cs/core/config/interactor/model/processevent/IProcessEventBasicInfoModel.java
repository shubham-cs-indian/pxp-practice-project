package com.cs.core.config.interactor.model.processevent;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IProcessEventBasicInfoModel extends IConfigEntityInformationModel {
  
  public static final String PROCESS_DEFINITION_ID = "processDefinitionId";
  
  public String getProcessDefinitionId();
  public void setProcessDefinitionId(String processDefinitionId);
}
