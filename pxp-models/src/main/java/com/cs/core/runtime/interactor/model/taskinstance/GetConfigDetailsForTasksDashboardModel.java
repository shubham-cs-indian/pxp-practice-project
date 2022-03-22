package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.role.Role;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.globalpermissions.ReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.interactor.model.task.TaskModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;
import java.util.Set;

public class GetConfigDetailsForTasksDashboardModel
    implements IGetConfigDetailsForTasksDashboardModel {
  
  private static final long                    serialVersionUID = 1L;
  
  protected IReferencedTemplatePermissionModel referencedPermissions;
  protected Map<String, ITaskModel>            referencedTasks;
  protected Set<String>                        roleIdsOfCurrentUser;
  protected Map<String, IRole>                 referencedRoles;
  protected Map<String, ITag>                  referencedTags;
  protected Set<String>                        personalTaskIds;
  
  @Override
  public IReferencedTemplatePermissionModel getReferencedPermissions()
  {
    return referencedPermissions;
  }
  
  @Override
  @JsonDeserialize(as = ReferencedTemplatePermissionModel.class)
  public void setReferencedPermissions(IReferencedTemplatePermissionModel referencedPermissions)
  {
    this.referencedPermissions = referencedPermissions;
  }
  
  @Override
  public Set<String> getRoleIdsOfCurrentUser()
  {
    return roleIdsOfCurrentUser;
  }
  
  @Override
  public void setRoleIdsOfCurrentUser(Set<String> roleIdsOfCurrentUser)
  {
    this.roleIdsOfCurrentUser = roleIdsOfCurrentUser;
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
  
  @Override
  public Map<String, IRole> getReferencedRoles()
  {
    return referencedRoles;
  }
  
  @Override
  @JsonDeserialize(contentAs = Role.class)
  public void setReferencedRoles(Map<String, IRole> referencedRoles)
  {
    this.referencedRoles = referencedRoles;
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
  public Set<String> getPersonalTaskIds()
  {
    return personalTaskIds;
  }
  
  @Override
  public void setPersonalTaskIds(Set<String> personalTaskIds)
  {
    this.personalTaskIds = personalTaskIds;
  }
}
