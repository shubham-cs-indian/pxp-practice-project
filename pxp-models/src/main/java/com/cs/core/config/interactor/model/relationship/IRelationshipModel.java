package com.cs.core.config.interactor.model.relationship;

import java.util.List;

import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface IRelationshipModel extends IConfigModel, IRelationship {
  
  public static final String ADDED_ATTRIBUTES           = "addedAttributes";
  public static final String ADDED_TAGS                 = "addedTags";
  
  public List<IModifiedRelationshipPropertyModel> getAddedAttributes();
  
  public void setAddedAttributes(List<IModifiedRelationshipPropertyModel> addedAttributes);
  
  public List<IModifiedRelationshipPropertyModel> getAddedTags();
  
  public void setAddedTags(List<IModifiedRelationshipPropertyModel> addedTags);
}
