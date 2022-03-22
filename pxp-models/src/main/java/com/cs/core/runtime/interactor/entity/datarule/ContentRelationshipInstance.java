package com.cs.core.runtime.interactor.entity.datarule;

import com.cs.core.runtime.interactor.entity.propertyinstance.IContentRelationshipInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRelationshipVersion;
import com.cs.core.runtime.interactor.entity.relationship.RelationshipVersion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

public class ContentRelationshipInstance implements IContentRelationshipInstance {
  
  private static final long            serialVersionUID = 1L;
  protected String                     relationshipMappingId;
  protected List<IRelationshipVersion> addedElements;
  protected List<String>               deletedElements;
  protected List<IRelationshipVersion> modifiedElements;
  protected List<IRelationshipVersion> modifiedContexts;
  protected List<IRelationshipVersion> elementIds;
  protected String                     id;
  protected String                     baseType;
  protected String                     sideId;
  
  @Override
  public String getRelationshipId()
  {
    return relationshipMappingId;
  }
  
  @Override
  public void setRelationshipId(String relationshipMappingId)
  {
    this.relationshipMappingId = relationshipMappingId;
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
  
  @JsonIgnore
  @Override
  public Long getVersionId()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionId(Long versionId)
  {
  }
  
  @JsonIgnore
  @Override
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @JsonIgnore
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
  
  @JsonIgnore
  @Override
  public String getLastModifiedBy()
  {
    return null;
  }
  
  @Override
  public List<IRelationshipVersion> getModifiedContexts()
  {
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
}
