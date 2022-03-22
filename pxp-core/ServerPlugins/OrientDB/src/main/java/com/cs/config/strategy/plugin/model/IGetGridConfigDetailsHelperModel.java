package com.cs.config.strategy.plugin.model;

import com.tinkerpop.blueprints.Vertex;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGetGridConfigDetailsHelperModel {
  
  public Set<Vertex> getEntityNodes();
  
  public void setEntityNodes(Set<Vertex> entityNodes);
  
  public Set<String> getEntityRids();
  
  public void setEntityRids(Set<String> entityRids);
  
  public Map<String, List<String>> getEntityVsInstanceIdsMap();
  
  public void setEntityVsInstanceIdsMap(Map<String, List<String>> entityVsInstanceIdsMap);
  
  public Set<String> getSelectedPropertyIds();
  
  public void setSelectedPropertyIds(Set<String> selectedPropertyIds);
  
  public String getRoleId();
  
  public void setRoleId(String roleId);
  
  public Set<String> getKlassIds();
  
  public void setKlassIds(Set<String> klassIds);
  
  public Map<String, Map<String, String>> getInstanceVsElementsCouplings();
  
  public void setInstanceVsElementsCouplings(
      Map<String, Map<String, String>> instanceVsElementsCouplings);
}
