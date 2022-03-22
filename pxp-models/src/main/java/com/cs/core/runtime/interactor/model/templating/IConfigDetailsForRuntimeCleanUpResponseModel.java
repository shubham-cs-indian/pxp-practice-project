package com.cs.core.runtime.interactor.model.templating;


import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IConfigDetailsForRuntimeCleanUpResponseModel extends IModel {
  
  public static final String TASK_IDS                                    = "taskIds";
  public static final String CONTEXT_IDS                                 = "contextIds";
  public static final String ATTRIBUTE_ID_VS_CONTEXT_IDS                 = "attributeIdVsContextIds";
  public static final String RELATIONSHIP_SIDE_ID_VS_REFERENCED_RELATION = "relationshipSideIdVsReferencedRelationship";
  public static final String NUMBER_OF_VERSIONS_TO_MAINTAIN              = "numberOfVersionsToMaintain";
  
  public List<String> getTaskIds();
  public void setTaskIds(List<String> taskIds);
  
  public List<String> getContextIds();
  public void setContextIds(List<String> contextIds);

  public Map<String, List<String>> getAttributeIdVsContextIds();
  public void setAttributeIdVsContextIds(Map<String, List<String>> attributeIdVsContextIds);
  
  public Map<String, Object> getRelationshipSideIdVsReferencedRelationship();
  public void setRelationshipSideIdVsReferencedRelationship(Map<String, Object> relationshipSideIdVsRelationshipCode);
  
  public Integer getNumberOfVersionsToMaintain();
  public void setNumberOfVersionsToMaintain(Integer numberOfVersionsToMaintain);
}
