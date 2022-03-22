package com.cs.core.runtime.interactor.model.instance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GetInstancesBasedOnTaskModel implements IGetInstancesBasedOnTaskModel {
  
  private static final long     serialVersionUID                    = 1L;
  protected String              roleId;
  protected String              currentUserId;
  protected Set<String>         klassIdsHavingRP                    = new HashSet<>();
  protected Map<String, Object> taskIdsForRolesHavingReadPermission = new HashMap<>();
  protected Set<String>         taskIdsHavingReadPermissions        = new HashSet<>();
  protected Set<String>         personalTaskIds                     = new HashSet<>();
  protected Set<String>         taxonomyIdsHavingRP                 = new HashSet<>();
  protected int                 from;
  protected int                 size;
  protected String              allSearch;
  
  @Override
  public String getRoleId()
  {
    return roleId;
  }
  
  @Override
  public void setRoleId(String roleId)
  {
    this.roleId = roleId;
  }
  
  @Override
  public String getCurrentUserId()
  {
    return currentUserId;
  }
  
  @Override
  public void setCurrentUserId(String currentUserId)
  {
    this.currentUserId = currentUserId;
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
  public int getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(int from)
  {
    this.from = from;
  }
  
  @Override
  public int getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(int size)
  {
    this.size = size;
  }
  
  @Override
  public String getAllSearch()
  {
    return allSearch;
  }
  
  @Override
  public void setAllSearch(String allSearch)
  {
    this.allSearch = allSearch;
  }
}
