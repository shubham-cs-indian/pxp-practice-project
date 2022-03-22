package com.cs.core.runtime.interactor.model.dashboard;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDashboardBGPStatusResponseModel extends IModel {

  public Long getFrom();
  public void setFrom(Long from);
  
  public Long getTotalCount();
  public void setTotalCount(Long totalCount);
  
  public List<IJobDataModel> getBgpJobs();
  public void setBgpJobs(List<IJobDataModel> bgpJobs);
  
  Map<String, String> getUsersMap();
  void setUsersMap(Map<String, String> usersMap);
  
  public String getService();
  public void setService(String service);
}
