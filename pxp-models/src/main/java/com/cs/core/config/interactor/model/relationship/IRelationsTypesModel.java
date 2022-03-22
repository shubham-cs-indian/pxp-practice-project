package com.cs.core.config.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IRelationsTypesModel extends IModel {
  
  String NATURE_REFERECE_TYPES     = "natureRefereceTypes";
  String NATURE_RELATIONSHIP_TYPES = "natureRelationshipTypes";
  
  public List<String> getNatureRefereceTypes();
  
  public void setNatureRefereceTypes(List<String> natureRefereceTypes);
  
  public List<String> getNatureRelationshipTypes();
  
  public void setNatureRelationshipTypes(List<String> natureRelationshipTypes);
}
