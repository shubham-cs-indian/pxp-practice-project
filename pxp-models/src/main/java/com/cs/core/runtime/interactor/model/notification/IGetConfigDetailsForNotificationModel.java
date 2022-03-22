package com.cs.core.runtime.interactor.model.notification;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.interactor.model.user.IUserInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetConfigDetailsForNotificationModel extends IModel {
  
  public static final String ROLE_ID_LABEL_MAP = "roleIdLabelMap";
  public static final String USER_LIST         = "userList";
  public static final String REFERENCED_TAGS   = "referencedTags";
  public static final String REFERENCED_TASKS  = "referencedTasks";
  
  public Map<String, String> getRoleIdLabelMap();
  
  public void setRoleIdLabelMap(Map<String, String> roleIdLabelMap);
  
  public List<IUserInformationModel> getUserList();
  
  public void setUserList(List<IUserInformationModel> userList);
  
  // key:tagId
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public Map<String, ITaskModel> getReferencedTasks();
  
  // key:taskId
  public void setReferencedTasks(Map<String, ITaskModel> referencedTasks);
}
