package com.cs.core.runtime.interactor.model.dashboard;

import java.util.List;
import java.util.Map;

public class DashboardBGPStatusResponseModel implements IDashboardBGPStatusResponseModel {
  
  private static final long     serialVersionUID = 1L;
  protected Long             from;
  protected Long             totalCount;
  protected List<IJobDataModel> bgpJobs;
  protected Map<String, String> usersMap;
  protected String 				service;
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
  public Long getTotalCount()
  {
    return totalCount;
  }
  
  @Override
  public void setTotalCount(Long totalCount)
  {
    this.totalCount = totalCount;
  }
  
  @Override
  public List<IJobDataModel> getBgpJobs()
  {
    return bgpJobs;
  }
  
  @Override
  public void setBgpJobs(List<IJobDataModel> bgpJobs)
  {
    this.bgpJobs = bgpJobs;
  }

  @Override
  public Map<String, String> getUsersMap()
  {
    return usersMap;
  }
  
  @Override
  public void setUsersMap(Map<String, String> usersMap)
  {
    this.usersMap = usersMap;
  }

	@Override
	public String getService() {
		return service;
	}

	@Override
	public void setService(String service) {
		this.service = service;
	}

}
