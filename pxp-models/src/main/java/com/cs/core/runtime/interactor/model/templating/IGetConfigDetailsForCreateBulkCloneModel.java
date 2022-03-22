package com.cs.core.runtime.interactor.model.templating;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;

import java.util.List;
import java.util.Map;

public interface IGetConfigDetailsForCreateBulkCloneModel extends IModel {
  
  public static final String REFERENCED_RELATIONSHIPS_PROPERTIES = "referencedRelationshipProperties";
  public static final String SIDE_IDS                            = "sideIds";
  
  // key:relationshipId
  public Map<String, IReferencedRelationshipPropertiesModel> getReferencedRelationshipProperties();
  
  public void setReferencedRelationshipProperties(
      Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties);
  
  public List<String> getSideIds();
  
  public void setSideIds(List<String> sideIds);
}
