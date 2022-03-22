package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetTaskInstanceForDashboardRequestModel
    implements IGetTaskInstanceForDashboardRequestModel {
  
  private static final long               serialVersionUID = 1L;
  IGetConfigDetailsForTasksDashboardModel configDetails;
  IIdParameterModel                       userDetails;
  
  @Override
  public IGetConfigDetailsForTasksDashboardModel getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDetailsForTasksDashboardModel.class)
  public void setConfigDetails(IGetConfigDetailsForTasksDashboardModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public IIdParameterModel getUserDetails()
  {
    return userDetails;
  }
  
  @Override
  @JsonDeserialize(as = IdParameterModel.class)
  public void setUserDetails(IIdParameterModel userDetails)
  {
    this.userDetails = userDetails;
  }
}
