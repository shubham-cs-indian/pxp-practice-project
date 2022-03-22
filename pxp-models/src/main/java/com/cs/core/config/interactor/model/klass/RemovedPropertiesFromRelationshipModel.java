package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.List;

public class RemovedPropertiesFromRelationshipModel
    implements IRemovedPropertiesFromRelationshipModel {
  
  private static final long serialVersionUID = 1L;
  protected String          sourceId;
  protected List<String>    removedDependentAttributesFromRelationship;
  protected List<String>    removedIndependentAttributesFromRelationship;
  protected List<String>    removedTagsFromRelationship;
  
  @Override
  public String getSourceId()
  {
    return sourceId;
  }
  
  @Override
  public void setSourceId(String sourceId)
  {
    this.sourceId = sourceId;
  }
  
  @Override
  public List<String> getRemovedDependentAttributesFromRelationship()
  {
    if (removedDependentAttributesFromRelationship == null) {
      removedDependentAttributesFromRelationship = new ArrayList<>();
    }
    return removedDependentAttributesFromRelationship;
  }
  
  @Override
  public void setRemovedDependentAttributesFromRelationship(
      List<String> removedDependentAttributesFromRelationship)
  {
    this.removedDependentAttributesFromRelationship = removedDependentAttributesFromRelationship;
  }
  
  @Override
  public List<String> getRemovedIndependentAttributesFromRelationship()
  {
    if (removedIndependentAttributesFromRelationship == null) {
      removedIndependentAttributesFromRelationship = new ArrayList<>();
    }
    return removedIndependentAttributesFromRelationship;
  }
  
  @Override
  public void setRemovedIndependentAttributesFromRelationship(
      List<String> removedIndependentAttributesFromRelationship)
  {
    this.removedIndependentAttributesFromRelationship = removedIndependentAttributesFromRelationship;
  }
  
  @Override
  public List<String> getRemovedTagsFromRelationship()
  {
    if (removedTagsFromRelationship == null) {
      removedTagsFromRelationship = new ArrayList<>();
    }
    return removedTagsFromRelationship;
  }
  
  @Override
  public void setRemovedTagsFromRelationship(List<String> removedTagsFromRelationship)
  {
    this.removedTagsFromRelationship = removedTagsFromRelationship;
  }
}
