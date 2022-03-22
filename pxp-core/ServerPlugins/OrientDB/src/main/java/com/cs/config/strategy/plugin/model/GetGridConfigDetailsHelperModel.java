package com.cs.config.strategy.plugin.model;

import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetGridConfigDetailsHelperModel implements IGetGridConfigDetailsHelperModel {
  
  protected Set<String>                      selectedPropertyIds;
  protected Set<Vertex>                      entityNodes;
  protected Set<String>                      entityRids;
  protected Map<String, List<String>>        entityVsInstanceIdsMap;
  protected String                           roleId;
  protected Set<String>                      klassIds;
  protected Map<String, Map<String, String>> instanceVsElementsCouplings;
  
  @Override
  public Set<Vertex> getEntityNodes()
  {
    if (entityNodes == null) {
      entityNodes = new HashSet<>();
    }
    return entityNodes;
  }
  
  @Override
  public void setEntityNodes(Set<Vertex> entityNodes)
  {
    this.entityNodes = entityNodes;
  }
  
  @Override
  public Set<String> getEntityRids()
  {
    if (entityRids == null) {
      entityRids = new HashSet<>();
    }
    return entityRids;
  }
  
  @Override
  public void setEntityRids(Set<String> entityRids)
  {
    this.entityRids = entityRids;
  }
  
  @Override
  public Map<String, List<String>> getEntityVsInstanceIdsMap()
  {
    if (entityVsInstanceIdsMap == null) {
      entityVsInstanceIdsMap = new HashMap<>();
    }
    return entityVsInstanceIdsMap;
  }
  
  @Override
  public void setEntityVsInstanceIdsMap(Map<String, List<String>> entityVsInstanceIdsMap)
  {
    this.entityVsInstanceIdsMap = entityVsInstanceIdsMap;
  }
  
  @Override
  public Set<String> getSelectedPropertyIds()
  {
    if (selectedPropertyIds == null) {
      selectedPropertyIds = new HashSet<>();
    }
    return selectedPropertyIds;
  }
  
  @Override
  public void setSelectedPropertyIds(Set<String> selectedPropertyIds)
  {
    this.selectedPropertyIds = selectedPropertyIds;
  }
  
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
  public Set<String> getKlassIds()
  {
    if (klassIds == null) {
      klassIds = new HashSet<>();
    }
    return klassIds;
  }
  
  @Override
  public void setKlassIds(Set<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
  @Override
  public Map<String, Map<String, String>> getInstanceVsElementsCouplings()
  {
    if (instanceVsElementsCouplings == null) {
      instanceVsElementsCouplings = new HashMap<>();
    }
    return instanceVsElementsCouplings;
  }
  
  @Override
  public void setInstanceVsElementsCouplings(
      Map<String, Map<String, String>> instanceVsElementsCouplings)
  {
    this.instanceVsElementsCouplings = instanceVsElementsCouplings;
  }
}
