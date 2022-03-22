package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesADMPropagationModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesToInheritModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class RelationshipPropertiesADMPropagationModel
    implements IRelationshipPropertiesADMPropagationModel {
  
  private static final long                       serialVersionUID = 1L;
  
  protected IRelationshipPropertiesToInheritModel modifiedProperties;
  protected IRelationshipPropertiesToInheritModel removedProperties;
  
  @Override
  public IRelationshipPropertiesToInheritModel getModifiedProperties()
  {
    return modifiedProperties;
  }
  
  @Override
  @JsonDeserialize(as = RelationshipPropertiesToInheritModel.class)
  public void setModifiedProperties(IRelationshipPropertiesToInheritModel modifiedProperties)
  {
    this.modifiedProperties = modifiedProperties;
  }
  
  @Override
  public IRelationshipPropertiesToInheritModel getRemovedProperties()
  {
    return removedProperties;
  }
  
  @Override
  @JsonDeserialize(as = RelationshipPropertiesToInheritModel.class)
  public void setRemovedProperties(IRelationshipPropertiesToInheritModel removedProperties)
  {
    this.removedProperties = removedProperties;
  }
}
