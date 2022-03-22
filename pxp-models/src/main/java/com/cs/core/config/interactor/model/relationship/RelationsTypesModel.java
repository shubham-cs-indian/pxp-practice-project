package com.cs.core.config.interactor.model.relationship;

import java.util.ArrayList;
import java.util.List;

public class RelationsTypesModel implements IRelationsTypesModel {
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    natureRefereceTypes;
  protected List<String>    natureRelationshipTypes;
  
  @Override
  public List<String> getNatureRefereceTypes()
  {
    if (natureRefereceTypes == null) {
      natureRefereceTypes = new ArrayList<String>();
    }
    return natureRefereceTypes;
  }
  
  @Override
  public void setNatureRefereceTypes(List<String> natureRefereceTypes)
  {
    this.natureRefereceTypes = natureRefereceTypes;
  }
  
  @Override
  public List<String> getNatureRelationshipTypes()
  {
    if (natureRelationshipTypes == null) {
      natureRelationshipTypes = new ArrayList<String>();
    }
    return natureRelationshipTypes;
  }
  
  @Override
  public void setNatureRelationshipTypes(List<String> natureRelationshipTypes)
  {
    this.natureRelationshipTypes = natureRelationshipTypes;
  }
}
