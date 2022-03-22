package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.config.interactor.entity.relationship.IRelationshipInstance;
import com.cs.core.config.interactor.entity.relationship.RelationshipInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRelationshipVersion;
import com.cs.core.runtime.interactor.entity.relationship.RelationshipVersion;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ContentRelationshipInstanceModel implements IContentRelationshipInstanceModel {
  
  private static final long             serialVersionUID = 1L;
  protected String                      relationshipId;
  protected List<IRelationshipVersion>  addedElements;
  protected List<String>                deletedElements;
  protected List<IRelationshipVersion>  modifiedElements;
  protected List<IRelationshipVersion>  modifiedContexts;
  protected List<IRelationshipVersion>  elementIds;
  protected String                      id;
  protected String                      baseType;
  protected String                      sideId;
  protected List<IRelationshipInstance> addedRelationshipInstances;
  protected List<IRelationshipInstance> addedNatureRelationshipInstances;
  
  @Override
  public String getRelationshipId()
  {
    return relationshipId;
  }
  
  @Override
  public void setRelationshipId(String relationshipMappingId)
  {
    this.relationshipId = relationshipMappingId;
  }
  
  @Override
  public List<IRelationshipVersion> getElementIds()
  {
    return elementIds;
  }
  
  @JsonDeserialize(contentAs = RelationshipVersion.class)
  @Override
  public void setElementIds(List<IRelationshipVersion> elementIds)
  {
    this.elementIds = elementIds;
  }
  
  @Override
  public List<IRelationshipVersion> getAddedElements()
  {
    if (addedElements == null) {
      addedElements = new ArrayList<>();
    }
    return addedElements;
  }
  
  @JsonDeserialize(contentAs = RelationshipVersion.class)
  @Override
  public void setAddedElements(List<IRelationshipVersion> addedElements)
  {
    this.addedElements = addedElements;
  }
  
  @Override
  public List<String> getDeletedElements()
  {
    if (deletedElements == null) {
      deletedElements = new ArrayList<>();
    }
    return deletedElements;
  }
  
  @Override
  public void setDeletedElements(List<String> deletedElements)
  {
    this.deletedElements = deletedElements;
  }
  
  @Override
  public List<IRelationshipVersion> getModifiedElements()
  {
    if (modifiedElements == null) {
      modifiedElements = new ArrayList<>();
    }
    return modifiedElements;
  }
  
  @JsonDeserialize(contentAs = RelationshipVersion.class)
  @Override
  public void setModifiedElements(List<IRelationshipVersion> modifiedElements)
  {
    this.modifiedElements = modifiedElements;
  }
  
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
  public List<IRelationshipVersion> getModifiedContexts()
  {
    if (modifiedContexts == null) {
      modifiedContexts = new ArrayList<>();
    }
    return modifiedContexts;
  }
  
  @JsonDeserialize(contentAs = RelationshipVersion.class)
  @Override
  public void setModifiedContexts(List<IRelationshipVersion> modifiedContexts)
  {
    this.modifiedContexts = modifiedContexts;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public String getSideId()
  {
    return sideId;
  }
  
  @Override
  public void setSideId(String sideId)
  {
    this.sideId = sideId;
  }
  
  @Override
  public List<IRelationshipInstance> getAddedRelationshipInstances()
  {
    if (addedRelationshipInstances == null) {
      addedRelationshipInstances = new ArrayList<>();
    }
    return addedRelationshipInstances;
  }
  
  @JsonDeserialize(contentAs = RelationshipInstance.class)
  @Override
  public void setAddedRelationshipInstances(List<IRelationshipInstance> addedRelationshipInstances)
  {
    this.addedRelationshipInstances = addedRelationshipInstances;
  }
  
  @Override
  public List<IRelationshipInstance> getAddedNatureRelationshipInstances()
  {
    if (addedNatureRelationshipInstances == null) {
      addedNatureRelationshipInstances = new ArrayList<>();
    }
    return addedNatureRelationshipInstances;
  }
  
  @JsonDeserialize(contentAs = RelationshipInstance.class)
  @Override
  public void setAddedNatureRelationshipInstances(
      List<IRelationshipInstance> addedNatureRelationshipInstances)
  {
    this.addedNatureRelationshipInstances = addedNatureRelationshipInstances;
  }
}
