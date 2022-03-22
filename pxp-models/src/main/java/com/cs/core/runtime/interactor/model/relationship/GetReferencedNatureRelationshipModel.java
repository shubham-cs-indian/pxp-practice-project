package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.config.interactor.entity.klass.KlassNatureRelationship;

import java.util.ArrayList;
import java.util.List;

public class GetReferencedNatureRelationshipModel extends KlassNatureRelationship
    implements IGetReferencedNatureRelationshipModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          propertyCollectionId;
  protected String          natureType;
  protected List<String>    tags             = new ArrayList<>();
  protected List<String>    attributes       = new ArrayList<>();
  
  @Override
  public String getPropertyCollectionId()
  {
    
    return propertyCollectionId;
  }
  
  @Override
  public void setPropertyCollectionId(String propertyCollectionId)
  {
    this.propertyCollectionId = propertyCollectionId;
  }
  
  @Override
  public String getNatureType()
  {
    
    return natureType;
  }
  
  @Override
  public void setNatureType(String natureType)
  {
    this.natureType = natureType;
  }
  
  @Override
  public List<String> getTags()
  {
    return tags;
  }
  
  @Override
  public void setTags(List<String> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public List<String> getAttributes()
  {
    return attributes;
  }
  
  @Override
  public void setAttributes(List<String> attributes)
  {
    this.attributes = attributes;
  }
}
