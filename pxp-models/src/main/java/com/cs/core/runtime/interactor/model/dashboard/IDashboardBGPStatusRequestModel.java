package com.cs.core.runtime.interactor.model.dashboard;

import java.util.Set;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.filter.SortModel;

public interface IDashboardBGPStatusRequestModel extends IModel {
  
  public static final String BGP_SERVICE  = "bgpService";
  public static final String USER_NAMES   = "userNames";
  public static final String MODULE_ID    = "moduleId";
  public static final String SORT_OPTIONS = "sortOptions";
  public static final String SIZE         = "size";
  public static final String FROM         = "from";
  public static final String JOB_STATUS   = "jobStatus";
  
  public String getBgpService();
  public void setBgpService(String bgpService);
  
  public Set<String> getUserNames();
  public void setUserNames(Set<String> userNames);
  
  String getJobStatus();
  void setJobStatus(String jobStatus);
  
  public String getModuleId();
  public void setModuleId(String moduleId);
  
  public Long getFrom();
  public void setFrom(Long from);
  
  public Integer getSize();
  public void setSize(Integer size);
  
  public SortModel getSortOptions();
  public void setSortOptions(SortModel sortOptions);
  
}