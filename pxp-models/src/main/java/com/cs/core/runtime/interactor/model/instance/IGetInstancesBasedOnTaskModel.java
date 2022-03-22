package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;
import java.util.Set;

public interface IGetInstancesBasedOnTaskModel extends IModel {
  
  public static final String ROLE_ID                                   = "roleId";
  public static final String CURRENT_USER_ID                           = "currentUserId";
  public static final String KLASS_IDS_HAVING_RP                       = "klassIdsHavingRP";
  public static final String TASK_IDS_FOR_ROLES_HAVING_READ_PERMISSION = "taskIdsForRolesHavingReadPermission";
  public static final String TASK_IDS_HAVING_READ_PERMISSION           = "taskIdsHavingReadPermissions";
  public static final String PERSONAL_TASK_IDS                         = "personalTaskIds";
  public static final String TAXONOMY_IDS_HAVING_RP                    = "taxonomyIdsHavingRP";
  public static final String FROM                                      = "from";
  public static final String SIZE                                      = "size";
  public static final String ALL_SEARCH                                = "allSearch";
  
  public String getRoleId();
  
  public void setRoleId(String roleId);
  
  public String getCurrentUserId();
  
  public void setCurrentUserId(String currentUserId);
  
  public Set<String> getKlassIdsHavingRP();
  
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingRP);
  
  public Map<String, Object> getTaskIdsForRolesHavingReadPermission();
  
  public void setTaskIdsForRolesHavingReadPermission(
      Map<String, Object> taskIdsForRolesHavingReadPermission);
  
  public Set<String> getTaskIdsHavingReadPermissions();
  
  public void setTaskIdsHavingReadPermissions(Set<String> taskIdsHavingReadPermissions);
  
  public Set<String> getPersonalTaskIds();
  
  public void setPersonalTaskIds(Set<String> personalTaskIds);
  
  public Set<String> getTaxonomyIdsHavingRP();
  
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP);
  
  public int getFrom();
  
  public void setFrom(int from);
  
  public int getSize();
  
  public void setSize(int size);
  
  public String getAllSearch();
  
  public void setAllSearch(String allSearch);
}
