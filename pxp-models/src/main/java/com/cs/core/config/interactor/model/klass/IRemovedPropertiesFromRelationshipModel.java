package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IRemovedPropertiesFromRelationshipModel extends IModel {
  
  public static final String SOURCE_ID                                       = "sourceId";
  public static final String REMOVED_DEPENDENT_ATTRIBUTE_FROM_RELATIONSHIP   = "removedDependentAttributesFromRelationship";
  public static final String REMOVED_INDEPENDENT_ATTRIBUTE_FROM_RELATIONSHIP = "removedIndependentAttributesFromRelationship";
  public static final String REMOVED_TAGS_FROM_RELATIONSHIP                  = "removedTagsFromRelationship";
  
  public String getSourceId();
  
  public void setSourceId(String sourceId);
  
  public List<String> getRemovedDependentAttributesFromRelationship();
  
  public void setRemovedDependentAttributesFromRelationship(
      List<String> removedDependentAttributesFromRelationship);
  
  public List<String> getRemovedIndependentAttributesFromRelationship();
  
  public void setRemovedIndependentAttributesFromRelationship(
      List<String> removedIndependentAttributesFromRelationship);
  
  public List<String> getRemovedTagsFromRelationship();
  
  public void setRemovedTagsFromRelationship(List<String> removedTagsFromRelationship);
}
