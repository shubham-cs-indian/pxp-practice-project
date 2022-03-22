package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;

import java.util.List;

public class RelationshipInstanceInformationModel implements IRelationshipInstanceInformationModel {
  
  protected String                                         id;
  protected String                                         relationshipMappingId;
  protected String                                         type;
  protected List<? extends IKlassInstanceInformationModel> elements;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getRelationshipMappingId()
  {
    return relationshipMappingId;
  }
  
  @Override
  public void setRelationshipMappingId(String relationshipMappingId)
  {
    this.relationshipMappingId = relationshipMappingId;
  }
  
  @Override
  public List<? extends IKlassInstanceInformationModel> getElements()
  {
    return elements;
  }
  
  @Override
  public void setElements(List<? extends IKlassInstanceInformationModel> elements)
  {
    this.elements = elements;
  }
}
