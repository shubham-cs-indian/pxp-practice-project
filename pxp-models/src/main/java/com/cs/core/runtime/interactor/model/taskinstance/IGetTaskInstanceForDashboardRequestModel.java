package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetTaskInstanceForDashboardRequestModel extends IModel {
  
  public static final String CONFIG_DETAIL = "configDetails";
  public static final String USER_DETAILS  = "userDetails";
  
  public IGetConfigDetailsForTasksDashboardModel getConfigDetails();
  
  public void setConfigDetails(IGetConfigDetailsForTasksDashboardModel configDetails);
  
  public IIdParameterModel getUserDetails();
  
  public void setUserDetails(IIdParameterModel userDetails);
}
