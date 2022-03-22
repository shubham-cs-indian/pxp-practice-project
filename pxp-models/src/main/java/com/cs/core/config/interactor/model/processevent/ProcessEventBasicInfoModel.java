package com.cs.core.config.interactor.model.processevent;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;

public class ProcessEventBasicInfoModel extends ConfigEntityInformationModel implements IProcessEventBasicInfoModel {

  private static final long serialVersionUID = 1L;
  protected String              processDefinitionId;
  
  @Override
  public String getProcessDefinitionId()
  {
    return processDefinitionId;
  }
  
  @Override
  public void setProcessDefinitionId(String processDefinitionId)
  {
    this.processDefinitionId = processDefinitionId;
  }
}
