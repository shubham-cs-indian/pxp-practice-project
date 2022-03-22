package com.cs.core.runtime.interactor.model.attribute;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.entity.datarule.AttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.datarule.IAttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.propertyinstance.DuplicateTypeAndInstanceIds;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IDuplicateTypeAndInstanceIds;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.model.instance.AbstractModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "baseType",
    visible = true, defaultImpl = ModifiedAttributeInstanceModel.class)
@JsonSubTypes({
    @Type(value = ModifiedAttributeInstanceModel.class,
        name = Constants.ATTRIBUTE_INSTANCE_PROPERTY_TYPE),
    @Type(value = ModifiedImageAttributeInstanceModel.class,
        name = Constants.IMAGE_ATTRIBUTE_INSTANCE_TYPE) })
public abstract class AbstractModifiedContentAttributeInstanceModel
    implements IModifiedContentAttributeInstanceModel {
  
  private static final long                        serialVersionUID = 1L;
  
  protected IContentAttributeInstance              entity;
  protected List<ITagInstance>                     addedTags        = new ArrayList<>();
  protected List<String>                           deletedTags      = new ArrayList<>();
  protected List<IModifiedContentTagInstanceModel> modifiedTags     = new ArrayList<>();
  
  public AbstractModifiedContentAttributeInstanceModel(
      IContentAttributeInstance contentAttributeInstance)
  {
    this.entity = contentAttributeInstance;
  }
  
  @Override
  public List<ITagInstance> getAddedTags()
  {
    return addedTags;
  }
  
  @JsonDeserialize(contentAs = TagInstance.class)
  @Override
  public void setAddedTags(List<ITagInstance> addedTags)
  {
    this.addedTags = addedTags;
  }
  
  @Override
  public List<String> getDeletedTags()
  {
    return deletedTags;
  }
  
  @Override
  public void setDeletedTags(List<String> deletedTags)
  {
    this.deletedTags = deletedTags;
  }
  
  @Override
  public List<IModifiedContentTagInstanceModel> getModifiedTags()
  {
    return modifiedTags;
  }
  
  @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
  @JsonDeserialize(contentAs = AbstractModifiedContentTagInstanceModel.class)
  @Override
  public void setModifiedTags(List<IModifiedContentTagInstanceModel> modifiedTags)
  {
    this.modifiedTags = modifiedTags;
  }
  
  @JsonIgnore
  @Override
  public List<? extends ITagInstance> getTags()
  {
    throw new RuntimeException("Not to bbe called on modified Model");
  }
  
  @JsonIgnore
  @Override
  public void setTags(List<? extends ITagInstance> tags)
  {
    throw new RuntimeException("Not to bbe called on modified Model");
  }
  
  @Override
  public IEntity getEntity()
  {
    return this.entity;
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    entity.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public Long getKlassInstanceVersion()
  {
    return entity.getKlassInstanceVersion();
  }
  
  @Override
  public void setKlassInstanceVersion(Long klassInstanceVersion)
  {
    entity.setKlassInstanceVersion(klassInstanceVersion);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public String getBaseType()
  {
    return entity.getBaseType();
  }
  
  @Override
  public void setBaseType(String type)
  {
    entity.setBaseType(type);
  }
  
  /*  @Override
  public String getMappingId()
  {
    return entity.getMappingId();
  }
  
  @JsonProperty("attributeMappingId")
  @Override
  public void setMappingId(String mappingId)
  {
    entity.setMappingId(mappingId);
  }*/
  
  @Override
  public Long getLastModified()
  {
    return entity.getLastModified();
  }
  
  @Override
  public void setLastModified(Long lastModified)
  {
    entity.setLastModified(lastModified);
  }
  
  public Map<String, Object> getNotification()
  {
    return entity.getNotification();
  }
  
  public void setNotification(Map<String, Object> notification)
  {
    entity.setNotification(notification);
  }
  
  @Override
  public List<IAttributeConflictingValue> getConflictingValues()
  {
    return entity.getConflictingValues();
  }
  
  @Override
  @JsonDeserialize(contentAs = AttributeConflictingValue.class)
  public void setConflictingValues(List<IAttributeConflictingValue> conflictingValues)
  {
    entity.setConflictingValues(conflictingValues);
  }
  
  @Override
  public String getAttributeId()
  {
    return entity.getAttributeId();
  }
  
  @Override
  public void setAttributeId(String attributeId)
  {
    entity.setAttributeId(attributeId);
  }
  
  @Override
  public String getKlassInstanceId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getVariantInstanceId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVariantInstanceId(String variantInstanceId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getCreatedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setCreatedBy(String createdBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getCreatedOn()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setCreatedOn(Long createdOn)
  {
    // TODO Auto-generated method stub
    
  }
  
  /*@Override
  public String getOwner()
  {
    // TODO Auto-generated method stub
    return null;
  }*/
  
  /*@Override
  public void setOwner(String owner)
  {
    // TODO Auto-generated method stub
  
  }*/
  
  @Override
  public String getJobId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setJobId(String jobId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public List<IDuplicateTypeAndInstanceIds> getDuplicateStatus()
  {
    return ((IContentAttributeInstance) entity).getDuplicateStatus();
  }
  
  @Override
  @JsonDeserialize(contentAs = DuplicateTypeAndInstanceIds.class)
  public void setDuplicateStatus(List<IDuplicateTypeAndInstanceIds> duplicateStatus)
  {
    ((IContentAttributeInstance) entity).setDuplicateStatus(duplicateStatus);
  }
  
  @Override
  public Integer getIsUnique()
  {
    return ((IContentAttributeInstance) entity).getIsUnique();
  }
  
  @Override
  public void setIsUnique(Integer isUnique)
  {
    ((IContentAttributeInstance) entity).setIsUnique(isUnique);
  }
}
