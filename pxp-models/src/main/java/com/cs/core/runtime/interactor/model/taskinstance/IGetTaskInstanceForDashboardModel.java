package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetTaskInstanceForDashboardModel extends IModel {
  
  public static final String TASK_INSTANCE_LIST         = "taskInstanceList";
  public static final String CONFIG_DETAILS             = "configDetails";
  public static final String TASKS_COUNT                = "tasksCount";
  public static final String UNREAD_NOTIFICATIONS_COUNT = "unreadNotificationsCount";
  
  public List<ITaskInstanceInformationModel> getTaskInstanceList();
  
  public void setTaskInstanceList(List<ITaskInstanceInformationModel> taskInstanceList);
  
  public IGetConfigDetailsForTasksDashboardModel getConfigDetails();
  
  public void setConfigDetails(IGetConfigDetailsForTasksDashboardModel klassDetails);
  
  public Integer getTasksCount();
  
  public void setTasksCount(Integer tasksCount);
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public Integer getUnreadNotificationsCount();
  
  public void setUnreadNotificationsCount(Integer unreadNotificationsCount);
}
