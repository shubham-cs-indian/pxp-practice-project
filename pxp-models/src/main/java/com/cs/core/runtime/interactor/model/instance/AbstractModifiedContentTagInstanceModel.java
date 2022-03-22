package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.model.tag.ModifiedTagInstanceModel;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "baseType",
    visible = true, defaultImpl = ModifiedTagInstanceModel.class)
@JsonSubTypes({
    @Type(value = ModifiedTagInstanceModel.class, name = "com.cs.core.runtime.interactor.entity.tag.TagInstance") })
public abstract class AbstractModifiedContentTagInstanceModel
    implements IModifiedContentTagInstanceModel {
  
  private static final long                        serialVersionUID  = 1L;
  
  protected IContentTagInstance                    entity;
  
  protected List<ITagInstanceValue>                addedTagValues    = new ArrayList<>();
  protected List<String>                           deletedTagValues  = new ArrayList<>();
  protected List<ITagInstanceValue>                modifiedTagValues = new ArrayList<>();
  protected List<IContentTagInstance>              addedTags         = new ArrayList<>();
  protected List<String>                           deletedTags       = new ArrayList<>();
  protected List<IModifiedContentTagInstanceModel> modifiedTags      = new ArrayList<>();
  
  public AbstractModifiedContentTagInstanceModel(IContentTagInstance contentTagInstance)
  {
    this.entity = contentTagInstance;
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
  public List<ITagInstanceValue> getTagValues()
  {
    return entity.getTagValues();
  }
  
  @Override
  @JsonDeserialize(contentAs = TagInstanceValue.class)
  public void setTagValues(List<ITagInstanceValue> tagValues)
  {
    entity.setTagValues(tagValues);
  }
  
  @Override
  public String getTagId()
  {
    return entity.getTagId();
  }
  
  @Override
  public void setTagId(String tagId)
  {
    entity.setTagId(tagId);
  }
  
  @Override
  public List<? extends ITagInstance> getTags()
  {
    return entity.getTags();
  }
  
  @Override
  public void setTags(List<? extends ITagInstance> tags)
  {
    entity.setTags(tags);
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
  
  @Override
  public Map<String, Object> getNotification()
  {
    return entity.getNotification();
  }
  
  @Override
  public void setNotification(Map<String, Object> notification)
  {
    entity.setNotification(notification);
  }
  
  @Override
  public List<IContentTagInstance> getAddedTags()
  {
    if (addedTags == null) {
      addedTags = new ArrayList<IContentTagInstance>();
    }
    return addedTags;
  }
  
  @JsonDeserialize(contentAs = TagInstance.class)
  @Override
  public void setAddedTags(List<IContentTagInstance> addedTags)
  {
    this.addedTags = addedTags;
  }
  
  @Override
  public List<String> getDeletedTags()
  {
    if (deletedTags == null) {
      deletedTags = new ArrayList<String>();
    }
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
    if (modifiedTags == null) {
      modifiedTags = new ArrayList<IModifiedContentTagInstanceModel>();
    }
    return modifiedTags;
  }
  
  @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
  @JsonDeserialize(contentAs = AbstractModifiedContentTagInstanceModel.class)
  @Override
  public void setModifiedTags(List<IModifiedContentTagInstanceModel> modifiedTags)
  {
    this.modifiedTags = modifiedTags;
  }
  
  @Override
  public List<ITagInstanceValue> getAddedTagValues()
  {
    if (addedTagValues == null) {
      addedTagValues = new ArrayList<ITagInstanceValue>();
    }
    return addedTagValues;
  }
  
  @JsonDeserialize(contentAs = TagInstanceValue.class)
  @Override
  public void setAddedTagValues(List<ITagInstanceValue> addedTagValues)
  {
    this.addedTagValues = addedTagValues;
  }
  
  @Override
  public List<String> getDeletedTagValues()
  {
    if (deletedTagValues == null) {
      deletedTagValues = new ArrayList<String>();
    }
    return deletedTagValues;
  }
  
  @Override
  public void setDeletedTagValues(List<String> deletedTagValues)
  {
    this.deletedTagValues = deletedTagValues;
  }
  
  @Override
  public List<ITagInstanceValue> getModifiedTagValues()
  {
    if (modifiedTagValues == null) {
      modifiedTagValues = new ArrayList<ITagInstanceValue>();
    }
    return modifiedTagValues;
  }
  
  @JsonDeserialize(contentAs = TagInstanceValue.class)
  @Override
  public void setModifiedTagValues(List<ITagInstanceValue> modifiedTagValues)
  {
    this.modifiedTagValues = modifiedTagValues;
  }
  
  @Override
  public List<ITagConflictingValue> getConflictingValues()
  {
    return entity.getConflictingValues();
  }
  
  @Override
  public void setConflictingValues(List<ITagConflictingValue> conflictingValues)
  {
    this.entity.setConflictingValues(conflictingValues);
  }
  
  @Override
  public String getKlassInstanceId()
  {
    return entity.getKlassInstanceId();
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    entity.setKlassInstanceId(klassInstanceId);
  }
  
  @Override
  public String getVariantInstanceId()
  {
    return entity.getVariantInstanceId();
  }
  
  @Override
  public void setVariantInstanceId(String variantInstanceId)
  {
    this.entity.getVariantInstanceId();
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
  public IEntity getEntity()
  {
    return entity;
  }
}
