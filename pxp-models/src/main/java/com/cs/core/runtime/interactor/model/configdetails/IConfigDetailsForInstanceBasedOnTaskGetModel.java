package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;
import java.util.Set;

public interface IConfigDetailsForInstanceBasedOnTaskGetModel extends IModel {
  
  public static final String ALLOWED_ENTITIES                          = "allowedEntities";
  public static final String KLASS_IDS_HAVING_RP                       = "klassIdsHavingRP";
  public static final String TAXONOMY_IDS_HAVING_RP                    = "taxonomyIdsHavingRP";
  public static final String TASK_IDS_FOR_ROLES_HAVING_READ_PERMISSION = "taskIdsForRolesHavingReadPermission";
  public static final String TASK_IDS_HAVING_READ_PERMISSION           = "taskIdsHavingReadPermissions";
  public static final String PERSONAL_TASK_IDS                         = "personalTaskIds";
  public static final String ROLE_ID_OF_CURRENT_USER                   = "roleIdOfCurrentUser";
  
  public Set<String> getAllowedEntities();
  
  public void setAllowedEntities(Set<String> allowedEntites);
  
  public Set<String> getKlassIdsHavingRP();
  
  public void setKlassIdsHavingRP(Set<String> klassIdsWithRP);
  
  public Set<String> getTaxonomyIdsHavingRP();
  
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP);
  
  public Map<String, Object> getTaskIdsForRolesHavingReadPermission();
  
  public void setTaskIdsForRolesHavingReadPermission(
      Map<String, Object> taskIdsForRolesHavingReadPermission);
  
  public Set<String> getTaskIdsHavingReadPermissions();
  
  public void setTaskIdsHavingReadPermissions(Set<String> taskIdsHavingReadPermissions);
  
  public Set<String> getPersonalTaskIds();
  
  public void setPersonalTaskIds(Set<String> personalTaskIds);
  
  public String getRoleIdOfCurrentUser();
  
  public void setRoleIdOfCurrentUser(String roleIdsOfCurrentUser);
}
