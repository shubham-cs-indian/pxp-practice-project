package com.cs.core.runtime.interactor.model.dashboard;

import java.util.Set;

import com.cs.core.runtime.interactor.model.filter.SortModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DashboardBGPStatusRequestModel implements IDashboardBGPStatusRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          bgpService;
  protected Set<String>     userNames;
  protected String          jobStatus;
  protected String          moduleId;
  protected Long            from;
  protected Integer         size;
  protected SortModel       sortOptions;
  
  @Override
  public String getBgpService()
  {
    return bgpService;
  }
  
  @Override
  public void setBgpService(String bgpService)
  {
    this.bgpService = bgpService;
  }
  
  @Override
  public Set<String> getUserNames()
  {
    return userNames;
  }
  
  @Override
  public void setUserNames(Set<String> userNames)
  {
    this.userNames = userNames;
  }
  
  @Override
  public String getJobStatus()
  {
    return jobStatus;
  }

  @Override
  public void setJobStatus(String jobStatus)
  {
    this.jobStatus = jobStatus;
  }

  @Override
  public String getModuleId()
  {
    return moduleId;
  }
  
  @Override
  public void setModuleId(String moduleId)
  {
    this.moduleId = moduleId;
  }

  @Override
  public Long getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Long from)
  {
    this.from = from;
  }
  
  @Override
  public Integer getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Integer size)
  {
    this.size = size;
  }
  
  @Override
  public SortModel getSortOptions()
  {
    return sortOptions;
  }
  
  @Override
  @JsonDeserialize(as = SortModel.class)
  public void setSortOptions(SortModel sortOptions)
  {
    this.sortOptions = sortOptions;
  }
}
