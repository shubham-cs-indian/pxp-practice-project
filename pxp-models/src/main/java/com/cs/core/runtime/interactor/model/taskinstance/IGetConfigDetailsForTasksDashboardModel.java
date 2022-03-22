package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;
import java.util.Set;

public interface IGetConfigDetailsForTasksDashboardModel extends IModel {
  
  public static final String ROLE_IDS_OF_CURRENT_USER = "roleIdsOfCurrentUser";
  public static final String REFERENCED_TASKS         = "referencedTasks";
  public static final String REFERENCED_ROLES         = "referencedRoles";
  public static final String REFERENCED_PERMISSIONS   = "referencedPermissions";
  public static final String REFERENCED_TAGS          = "referencedTags";
  public static final String PERSONAL_TASK_IDS        = "personalTaskIds";
  
  public IReferencedTemplatePermissionModel getReferencedPermissions();
  
  public void setReferencedPermissions(IReferencedTemplatePermissionModel referencedPermissions);
  
  public Set<String> getRoleIdsOfCurrentUser();
  
  public void setRoleIdsOfCurrentUser(Set<String> roleIdsOfCurrentUser);
  
  public Map<String, ITaskModel> getReferencedTasks();
  
  public void setReferencedTasks(Map<String, ITaskModel> referencedTasks);
  
  public Map<String, IRole> getReferencedRoles();
  
  public void setReferencedRoles(Map<String, IRole> referencedRoles);
  
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public Set<String> getPersonalTaskIds();
  
  public void setPersonalTaskIds(Set<String> personalTaskIds);
}
