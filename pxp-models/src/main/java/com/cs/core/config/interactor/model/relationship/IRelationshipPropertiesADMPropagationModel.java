package com.cs.core.config.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IRelationshipPropertiesADMPropagationModel extends IModel {
  
  public static final String MODIFIED_PROPERTIES = "modifiedProperties";
  public static final String REMOVED_PROPERTIES  = "removedProperties";
  
  public IRelationshipPropertiesToInheritModel getModifiedProperties();
  
  public void setModifiedProperties(IRelationshipPropertiesToInheritModel modifiedProperties);
  
  public IRelationshipPropertiesToInheritModel getRemovedProperties();
  
  public void setRemovedProperties(IRelationshipPropertiesToInheritModel removedProperties);
}
