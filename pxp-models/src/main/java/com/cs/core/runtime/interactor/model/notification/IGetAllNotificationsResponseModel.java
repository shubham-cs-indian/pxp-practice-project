package com.cs.core.runtime.interactor.model.notification;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.interactor.model.user.IUserInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetAllNotificationsResponseModel extends IModel {
  
  public static final String NOTIFICATION               = "notifications";
  public static final String TASK_INFO                  = "taskInfo";
  public static final String USER_INFO_LIST             = "userInfoList";
  public static final String FROM                       = "from";
  public static final String SIZE                       = "size";
  public static final String ROLE_ID_LABEL_MAP          = "roleIdLabelMap";
  public static final String UNREAD_NOTIFICATIONS_COUNT = "unreadNotificationsCount";
  public static final String REFERENCED_TAGS            = "referencedTags";
  public static final String REFERENCED_TASKS           = "referencedTasks";
  
  public List<INotificationModel> getNotifications();
  
  public void setNotifications(List<INotificationModel> notifications);
  
  public List<IIdAndTypeModel> getTaskInfo();
  
  public void setTaskInfo(List<IIdAndTypeModel> taskInfo);
  
  public List<IUserInformationModel> getUserInfoList();
  
  public void setUserInfoList(List<IUserInformationModel> userInfoList);
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
  
  public Integer getSize();
  
  public void setSize(Integer size);
  
  public Map<String, String> getRoleIdLabelMap();
  
  public void setRoleIdLabelMap(Map<String, String> roleIdLabelMap);
  
  public Integer getUnreadNotificationsCount();
  
  public void setUnreadNotificationsCount(Integer unreadNotificationsCount);
  
  // key:tagId
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public Map<String, ITaskModel> getReferencedTasks();
  
  // key:taskId
  public void setReferencedTasks(Map<String, ITaskModel> referencedTasks);
}
