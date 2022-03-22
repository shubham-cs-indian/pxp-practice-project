package com.cs.core.config.plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigDetailsRuntimeCleanupHelperModel
    implements IConfigDetailsRuntimeCleanupHelperModel {
  
  protected Set<String>               applicableTaskIds;
  protected Set<String>               applicableContextIds;
  protected Set<String>               applicableRelationshipSideIds;
  protected Map<String, List<String>> applicableAttributeIdVsContextIds;
  protected Set<String>               removedTaskIds;
  protected Set<String>               removedRelationshipSideIds;
  protected Set<String>               removedContextIds;
  protected Map<String, List<String>> removedAttributeIdVsContextIds;
  protected Map<String, Object>       relationshipSideIdVsReferencedRelationships;
  protected Integer                   numberOfVersionsToMaintain;
  
  @Override
  public Set<String> getApplicableTaskIds()
  {
    if (applicableTaskIds == null) {
      applicableTaskIds = new HashSet<String>();
    }
    return applicableTaskIds;
  }
  
  @Override
  public void setApplicableTaskIds(Set<String> applicableTaskIds)
  {
    this.applicableTaskIds = applicableTaskIds;
  }
  
  
  @Override
  public Set<String> getApplicableContextIds()
  {
    if (applicableContextIds == null) {
      applicableContextIds = new HashSet<String>();
    }
    return applicableContextIds;
  }
  
  @Override
  public void setApplicableContextIds(Set<String> applicableContextIds)
  {
    this.applicableContextIds = applicableContextIds;
  }
  
  @Override
  public Map<String, List<String>> getApplicableAttributeIdVsContextIds()
  {
    if (applicableAttributeIdVsContextIds == null) {
      applicableAttributeIdVsContextIds = new HashMap<String, List<String>>();
    }
    return applicableAttributeIdVsContextIds;
  }
  
  @Override
  public void setApplicableAttributeIdVsContextIds(
      Map<String, List<String>> applicableAttributeIdVsContextIds)
  {
    this.applicableAttributeIdVsContextIds = applicableAttributeIdVsContextIds;
  }
  
  @Override
  public Set<String> getApplicableRelationshipSideIds()
  {
    if (applicableRelationshipSideIds == null) {
      applicableRelationshipSideIds = new HashSet<String>();
    }
    return applicableRelationshipSideIds;
  }
  
  @Override
  public void setApplicableRelationshipSideIds(Set<String> applicableRelationshipSideIds)
  {
    this.applicableRelationshipSideIds = applicableRelationshipSideIds;
  }
  
  @Override
  public Set<String> getRemovedTaskIds()
  {
    if (removedTaskIds == null) {
      removedTaskIds = new HashSet<String>();
    }
    return removedTaskIds;
  }
  
  @Override
  public void setRemovedTaskIds(Set<String> removedTaskIds)
  {
    this.removedTaskIds = removedTaskIds;
  }
  
  
  @Override
  public Set<String> getRemovedContextIds()
  {
    if (removedContextIds == null) {
      removedContextIds = new HashSet<String>();
    }
    return removedContextIds;
  }
  
  @Override
  public void setRemovedContextIds(Set<String> removedContextIds)
  {
    this.removedContextIds = removedContextIds;
  }
  
  @Override
  public Set<String> getRemovedRelationshipSideIds()
  {
    if (removedRelationshipSideIds == null) {
      removedRelationshipSideIds = new HashSet<>();
    }
    
    return removedRelationshipSideIds;
  }
  
  @Override
  public void setRemovedRelationshipSideIds(Set<String> removedRelationshipSideIds)
  {
    this.removedRelationshipSideIds = removedRelationshipSideIds;
  }
  
  @Override
  public Map<String, List<String>> getRemovedAttributeIdVsContextIds()
  {
    if (removedAttributeIdVsContextIds == null) {
      removedAttributeIdVsContextIds = new HashMap<String, List<String>>();
    }
    return removedAttributeIdVsContextIds;
  }
  
  @Override
  public void setRemovedAttributeIdVsContextIds(
      Map<String, List<String>> removedAttributeIdVsContextIds)
  {
    this.removedAttributeIdVsContextIds = removedAttributeIdVsContextIds;
  }
  
  @Override
  public Map<String, Object> getRelationshipSideIdVsReferencedRelationships()
  {
    if (relationshipSideIdVsReferencedRelationships == null) {
      relationshipSideIdVsReferencedRelationships = new HashMap<>();
    }
    return relationshipSideIdVsReferencedRelationships;
  }
  
  @Override
  public void setRelationshipSideIdVsReferencedRelationships(
      Map<String, Object> relationshipSideIdVsReferencedRelationships)
  {
    this.relationshipSideIdVsReferencedRelationships = relationshipSideIdVsReferencedRelationships;
  }
  
  @Override
  public Integer getNumberOfVersionsToMaintain()
  {
    return numberOfVersionsToMaintain;
  }
  
  @Override
  public void setNumberOfVersionsToMaintain(Integer numberOfVersionsToMaintain)
  {
    this.numberOfVersionsToMaintain = numberOfVersionsToMaintain;
  }
}
