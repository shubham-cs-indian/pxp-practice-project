package com.cs.core.runtime.interactor.model.templating;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.model.klass.IConflictingValueModel;

public interface IGetConfigDetailsForTasksTabModel extends IGetConfigDetailsModel {
  
  String CONFLICTING_VALUES                       = "conflictingValues";
  String ROLE_IDS_HAVING_READ_PERMISSION_FOR_TASK = "roleIdsHavingReadPermissionsForTask";
  String ROLE_ID_OF_CURRENT_USER                  = "roleIdOfCurrentUser";
  String TASK_IDS_HAVING_READ_PERMISSION          = "taskIdsHavingReadPermissions";
  String REFERENCED_ROLES                         = "referencedRoles";
  String SIDE2_LINKED_VARIANT_KR_IDS              = "side2LinkedVariantKrIds";
  String LINKED_VARIANT_CODES                     = "linkedVariantCodes";
  
  public Map<String, IConflictingValueModel> getConflictingValues();
  
  public void setConflictingValues(Map<String, IConflictingValueModel> conflictingValues);
  
  public Set<String> getRoleIdsHavingReadPermissionsForTask();
  
  public void setRoleIdsHavingReadPermissionsForTask(
      Set<String> roleIdsHavingReadPermissionsForTask);
  
  public String getRoleIdOfCurrentUser();
  
  public void setRoleIdOfCurrentUser(String roleIdsOfCurrentUser);
  
  public Set<String> getTaskIdsHavingReadPermissions();
  
  public void setTaskIdsHavingReadPermissions(Set<String> taskIdsHavingReadPermissions);
  
  public Map<String, IRole> getReferencedRoles();
  
  public void setReferencedRoles(Map<String, IRole> referencedRoles);
  
  public List<String> getSide2LinkedVariantKrIds();
  
  public void setSide2LinkedVariantKrIds(List<String> side2LinkedVariantKrIds);
  
  public List<String> getLinkedVariantCodes();
  
  public void setLinkedVariantCodes(List<String> linkedVariantCodes);
}
