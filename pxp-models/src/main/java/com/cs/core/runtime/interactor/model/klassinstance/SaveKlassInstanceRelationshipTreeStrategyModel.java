package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.entity.propertyinstance.IContentRelationshipInstance;

import java.util.List;

public class SaveKlassInstanceRelationshipTreeStrategyModel
    implements ISaveKlassInstanceRelationshipTreeStrategyModel {
  
  protected String                             id;
  protected String                             type;
  protected List<IContentRelationshipInstance> addedRelationships;
  protected List<IContentRelationshipInstance> deletedRelationships;
  protected List<IContentRelationshipInstance> modifiedRelationships;
  
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
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public List<IContentRelationshipInstance> getAddedRelationships()
  {
    return addedRelationships;
  }
  
  @Override
  public void setAddedRelationships(List<IContentRelationshipInstance> addedRelationships)
  {
    this.addedRelationships = addedRelationships;
  }
  
  @Override
  public List<IContentRelationshipInstance> getDeletedRelationships()
  {
    return deletedRelationships;
  }
  
  @Override
  public void setDeletedRelationships(List<IContentRelationshipInstance> deletedRelationships)
  {
    this.deletedRelationships = deletedRelationships;
  }
  
  @Override
  public List<IContentRelationshipInstance> getModifiedRelationships()
  {
    return modifiedRelationships;
  }
  
  @Override
  public void setModifiedRelationships(List<IContentRelationshipInstance> modifiedRelationships)
  {
    this.modifiedRelationships = modifiedRelationships;
  }
}
