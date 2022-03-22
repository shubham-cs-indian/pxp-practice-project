package com.cs.core.runtime.interactor.model.configdetails;

import java.util.Map;
import java.util.Set;

public class ConfigDetailsForInstanceBasedOnTaskGetModel
    implements IConfigDetailsForInstanceBasedOnTaskGetModel {
  
  private static final long     serialVersionUID = 1L;
  
  protected Set<String>         allowedEntities;
  protected Set<String>         klassIdsHavingRP;
  protected Set<String>         taxonomyIdsHavingRP;
  protected Map<String, Object> taskIdsForRolesHavingReadPermission;
  protected Set<String>         taskIdsHavingReadPermissions;
  protected Set<String>         personalTaskIds;
  protected String              roleIdOfCurrentUser;
  
  @Override
  public Set<String> getAllowedEntities()
  {
    return allowedEntities;
  }
  
  @Override
  public void setAllowedEntities(Set<String> allowedEntities)
  {
    this.allowedEntities = allowedEntities;
  }
  
  @Override
  public Set<String> getKlassIdsHavingRP()
  {
    return klassIdsHavingRP;
  }
  
  @Override
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingRP)
  {
    this.klassIdsHavingRP = klassIdsHavingRP;
  }
  
  @Override
  public Set<String> getTaxonomyIdsHavingRP()
  {
    return taxonomyIdsHavingRP;
  }
  
  @Override
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP)
  {
    this.taxonomyIdsHavingRP = taxonomyIdsHavingRP;
  }
  
  @Override
  public Set<String> getTaskIdsHavingReadPermissions()
  {
    return taskIdsHavingReadPermissions;
  }
  
  @Override
  public void setTaskIdsHavingReadPermissions(Set<String> taskIdsHavingReadPermissions)
  {
    this.taskIdsHavingReadPermissions = taskIdsHavingReadPermissions;
  }
  
  @Override
  public Map<String, Object> getTaskIdsForRolesHavingReadPermission()
  {
    return taskIdsForRolesHavingReadPermission;
  }
  
  @Override
  public void setTaskIdsForRolesHavingReadPermission(
      Map<String, Object> taskIdsForRolesHavingReadPermission)
  {
    this.taskIdsForRolesHavingReadPermission = taskIdsForRolesHavingReadPermission;
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
  
  @Override
  public String getRoleIdOfCurrentUser()
  {
    return roleIdOfCurrentUser;
  }
  
  @Override
  public void setRoleIdOfCurrentUser(String roleIdOfCurrentUser)
  {
    this.roleIdOfCurrentUser = roleIdOfCurrentUser;
  }
}
