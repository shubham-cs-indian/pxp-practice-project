package com.cs.core.runtime.interactor.model.notification;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.interactor.model.task.TaskModel;
import com.cs.core.config.interactor.model.user.IUserInformationModel;
import com.cs.core.config.interactor.model.user.UserInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetConfigDetailsForNotificationModel implements IGetConfigDetailsForNotificationModel {
  
  private static final long             serialVersionUID = 1L;
  
  protected Map<String, String>         roleIdLabelMap   = new HashMap<>();
  protected List<IUserInformationModel> userList         = new ArrayList<>();
  protected Map<String, ITag>           referencedTags   = new HashMap<>();
  protected Map<String, ITaskModel>     referencedTasks  = new HashMap<>();
  
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
  public List<IUserInformationModel> getUserList()
  {
    return userList;
  }
  
  @JsonDeserialize(contentAs = UserInformationModel.class)
  @Override
  public void setUserList(List<IUserInformationModel> userList)
  {
    this.userList = userList;
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
