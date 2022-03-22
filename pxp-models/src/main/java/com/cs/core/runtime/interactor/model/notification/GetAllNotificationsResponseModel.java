package com.cs.core.runtime.interactor.model.notification;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.interactor.model.task.TaskModel;
import com.cs.core.config.interactor.model.user.IUserInformationModel;
import com.cs.core.config.interactor.model.user.UserInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IdAndTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllNotificationsResponseModel implements IGetAllNotificationsResponseModel {
  
  private static final long             serialVersionUID = 1L;
  
  protected List<INotificationModel>    notifications    = new ArrayList<>();
  protected List<IIdAndTypeModel>       taskInfo         = new ArrayList<>();
  protected Map<String, String>         roleIdLabelMap   = new HashMap<>();
  protected List<IUserInformationModel> userInfoList     = new ArrayList<>();
  protected Integer                     from;
  protected Integer                     size;
  protected Integer                     unreadNotificationsCount;
  protected Map<String, ITag>           referencedTags   = new HashMap<>();
  protected Map<String, ITaskModel>     referencedTasks  = new HashMap<>();
  
  @Override
  public List<INotificationModel> getNotifications()
  {
    return notifications;
  }
  
  @JsonDeserialize(contentAs = NotificationModel.class)
  @Override
  public void setNotifications(List<INotificationModel> notifications)
  {
    this.notifications = notifications;
  }
  
  @Override
  public List<IIdAndTypeModel> getTaskInfo()
  {
    if(taskInfo == null) {
      taskInfo = new ArrayList<>();
    }
    return taskInfo;
  }
  
  @JsonDeserialize(contentAs = IdAndTypeModel.class)
  @Override
  public void setTaskInfo(List<IIdAndTypeModel> taskInfo)
  {
    this.taskInfo = taskInfo;
  }
  
  @Override
  public List<IUserInformationModel> getUserInfoList()
  {
    return userInfoList;
  }
  
  @JsonDeserialize(contentAs = UserInformationModel.class)
  @Override
  public void setUserInfoList(List<IUserInformationModel> userInfoList)
  {
    this.userInfoList = userInfoList;
  }
  
  @Override
  public Integer getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Integer from)
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
  public Map<String, String> getRoleIdLabelMap()
  {
    return roleIdLabelMap;
  }
  
  @Override
  public void setRoleIdLabelMap(Map<String, String> roleIdLabelMap)
  {
    this.roleIdLabelMap = roleIdLabelMap;
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
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = Tag.class)
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public Map<String, ITaskModel> getReferencedTasks()
  {
    return referencedTasks;
  }
  
  @JsonDeserialize(contentAs = TaskModel.class)
  @Override
  public void setReferencedTasks(Map<String, ITaskModel> referencedTasks)
  {
    this.referencedTasks = referencedTasks;
  }
}
