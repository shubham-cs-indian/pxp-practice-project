package com.cs.core.runtime.interactor.model.taskinstance;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetTaskInstanceForDashboardResponseModel implements IGetTaskInstanceForDashboardModel {
  
  private static final long                         serialVersionUID         = 1L;
  
  protected Long                                    count;
  
  protected List<ITaskInstanceInformationModel>     taskInstanceList;
  protected IGetConfigDetailsForTasksDashboardModel configDetails;
  protected Integer                                 tasksCount               = 0;
  protected Integer                                 unreadNotificationsCount = 0;
  
  @Override
  public List<ITaskInstanceInformationModel> getTaskInstanceList()
  {
    if(taskInstanceList == null) {
      taskInstanceList = new ArrayList<ITaskInstanceInformationModel>();
    }
    return taskInstanceList;
  }
  
  @JsonDeserialize(contentAs = TaskInstanceInformationModel.class)
  @Override
  public void setTaskInstanceList(List<ITaskInstanceInformationModel> taskInstanceList)
  {
    this.taskInstanceList = taskInstanceList;
  }
  
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
  public Integer getTasksCount()
  {
    return tasksCount;
  }
  
  @Override
  public void setTasksCount(Integer tasksCount)
  {
    this.tasksCount = tasksCount;
  }
  
  @Override
  public Long getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
  
  @Override
  public Integer getUnreadNotificationsCount()
  {
    return unreadNotificationsCount;
  }
  
  @Override
  public void setUnreadNotificationsCount(Integer unreadNotificationsCount)
  {
    this.unreadNotificationsCount = unreadNotificationsCount;
  }
}
