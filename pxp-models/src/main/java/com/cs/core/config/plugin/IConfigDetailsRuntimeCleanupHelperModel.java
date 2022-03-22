package com.cs.core.config.plugin;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IConfigDetailsRuntimeCleanupHelperModel {
  
  public Set<String> getApplicableTaskIds();
  
  public void setApplicableTaskIds(Set<String> applicableTaskIds);
  
  public Set<String> getApplicableContextIds();
  
  public void setApplicableContextIds(Set<String> applicableContextIds);
  
  // Key is AttributeId and values is list of contextIds
  public Map<String, List<String>> getApplicableAttributeIdVsContextIds();
  
  public void setApplicableAttributeIdVsContextIds(
      Map<String, List<String>> applicableAttributeIdVsContextIds);
  
  public Set<String> getApplicableRelationshipSideIds();
  
  public void setApplicableRelationshipSideIds(Set<String> applicableRelationshipSideIds);
  
  public Set<String> getRemovedTaskIds();
  
  public void setRemovedTaskIds(Set<String> removedTaskIds);
  
  public Set<String> getRemovedContextIds();
  
  public void setRemovedContextIds(Set<String> removedContextIds);
  
  public Set<String> getRemovedRelationshipSideIds();
  
  public void setRemovedRelationshipSideIds(Set<String> removedRelationshipSideIds);
  
  // Key is AttributeId and values is list of contextIds
  public Map<String, List<String>> getRemovedAttributeIdVsContextIds();
  
  public void setRemovedAttributeIdVsContextIds(
      Map<String, List<String>> removedattributeIdVsContextIds);
  
  public Map<String, Object> getRelationshipSideIdVsReferencedRelationships();
  
  public void setRelationshipSideIdVsReferencedRelationships(
      Map<String, Object> relationshipSideIdVsReferencedRelationships);
  
  public Integer getNumberOfVersionsToMaintain();
  
  public void setNumberOfVersionsToMaintain(Integer numberOfVersionsToMaintain);
}