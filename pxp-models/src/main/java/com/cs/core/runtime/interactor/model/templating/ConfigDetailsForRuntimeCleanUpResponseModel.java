package com.cs.core.runtime.interactor.model.templating;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigDetailsForRuntimeCleanUpResponseModel implements IConfigDetailsForRuntimeCleanUpResponseModel {
  
  private static final long           serialVersionUID = 1L;
  protected List<String>              taskIds;
  protected List<String>              relationshipSideIds;
  protected List<String>              contextIds;
  protected Map<String, List<String>> attributeIdVsContextIds;
  protected Map<String, Object>       relationshipSideIdVsReferencedRelationship;
  protected Integer                   numberOfVersionsToMaintain;
  
  @Override
  public List<String> getTaskIds()
  {
    if(taskIds == null) {
      taskIds = new ArrayList<>();
    }
    return taskIds;
  }
  
  @Override
  public void setTaskIds(List<String> taskIds)
  {
    this.taskIds = taskIds;
  }

  @Override
  public List<String> getContextIds()
  {
    if(contextIds == null) {
      contextIds = new ArrayList<>();
    }
    return contextIds;
  }
  
  @Override
  public void setContextIds(List<String> contextIds)
  {
    this.contextIds = contextIds;
  }

  @Override
  public Map<String, List<String>> getAttributeIdVsContextIds()
  {
    if(attributeIdVsContextIds == null) {
      attributeIdVsContextIds = new HashMap<String, List<String>>();
    }
    return attributeIdVsContextIds;
  }

  @Override
  public void setAttributeIdVsContextIds(Map<String, List<String>> attributeIdVsContextIds)
  {
    this.attributeIdVsContextIds = attributeIdVsContextIds;
  }
  
  @Override
  public Map<String, Object> getRelationshipSideIdVsReferencedRelationship()
  {
    return relationshipSideIdVsReferencedRelationship;
  }
  
  @Override
  public void setRelationshipSideIdVsReferencedRelationship(Map<String, Object> relationshipSideIdVsReferencedRelationship)
  {
    this.relationshipSideIdVsReferencedRelationship = relationshipSideIdVsReferencedRelationship;
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