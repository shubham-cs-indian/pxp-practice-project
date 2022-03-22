package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.cs.core.runtime.interactor.model.instance.AbstractModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedAssetAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = Id.NONE)
public class ModifiedAssetAttributeInstanceModel implements IModifiedAssetAttributeInstanceModel {
  
  private static final long                        serialVersionUID = 1L;
  
  protected String                                 id;
  protected String                                 attributeId;
  protected String                                 assetInstanceId;
  protected Boolean                                isDefault        = false;
  protected String                                 fileName;
  protected String                                 description;
  protected Boolean                                isValueChanged   = false;
  protected Long                                   versionId        = 0l;
  protected String                                 variantOf;
  protected String                                 cloneOf;
  protected Map<String, Object>                    notification;
  protected Long                                   versionTimestamp;
  protected Long                                   klassInstanceVersion;
  protected List<Long>                             existingVersions;
  protected String                                 baseType         = this.getClass()
      .getName();
  protected String                                 lastModifiedBy;
  protected List<ITagInstance>                     tags             = new ArrayList<>();
  protected List<ITagInstance>                     addedTags;
  protected List<String>                           deletedTags;
  protected List<IModifiedContentTagInstanceModel> modifiedTags;
  protected Long                                   lastModified;
  protected Boolean                                shouldMerge      = true;
  protected String                                 type;
  protected String                                 contextInstanceId;
  protected String                                 relationshipId;
  protected Long                                   iid;
  
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
  public List<? extends ITagInstance> getTags()
  {
    return tags;
  }
  
  @Override
  public void setTags(List<? extends ITagInstance> tags)
  {
    this.tags = (List<ITagInstance>) tags;
  }
  
  @Override
  public String getAssetInstanceId()
  {
    return assetInstanceId;
  }
  
  @Override
  public void setAssetInstanceId(String assetInstanceId)
  {
    this.assetInstanceId = assetInstanceId;
  }
  
  @Override
  public String getAttributeId()
  {
    return attributeId;
  }
  
  @Override
  public void setAttributeId(String attributeId)
  {
    this.attributeId = attributeId;
  }
  
  @Override
  public Boolean getIsDefault()
  {
    return isDefault;
  }
  
  @Override
  public void setIsDefault(Boolean isDefault)
  {
    this.isDefault = isDefault;
  }
  
  @Override
  public String getFileName()
  {
    return fileName;
  }
  
  @Override
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
  
  @Override
  public String getDescription()
  {
    return description;
  }
  
  @Override
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  @Override
  public Long getKlassInstanceVersion()
  {
    return klassInstanceVersion;
  }
  
  @Override
  public void setKlassInstanceVersion(Long klassInstanceVersion)
  {
    this.klassInstanceVersion = klassInstanceVersion;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String type)
  {
    this.baseType = type;
  }
  
  /*  @Override
  public String getMappingId()
  {
    return mappingId;
  }
  
  @Override
  public void setMappingId(String mappingId)
  {
    this.mappingId = mappingId;
  }*/
  
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
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public List<ITagInstance> getAddedTags()
  {
    return addedTags;
  }
  
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
  
  @Override
  @JsonTypeInfo(use = Id.NONE)
  @JsonDeserialize(contentAs = AbstractModifiedContentTagInstanceModel.class)
  public void setModifiedTags(List<IModifiedContentTagInstanceModel> modifiedTags)
  {
    this.modifiedTags = modifiedTags;
  }
  
  @Override
  public IEntity getEntity()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public Long getLastModified()
  {
    return lastModified;
  }
  
  @Override
  public void setLastModified(Long lastModified)
  {
    this.lastModified = lastModified;
  }
  
  @Override
  public Map<String, Object> getNotification()
  {
    return notification;
  }
  
  @Override
  public void setNotification(Map<String, Object> notification)
  {
    this.notification = notification;
  }
  
  @Override
  public Boolean getShouldMerge()
  {
    return shouldMerge;
  }
  
  @Override
  public Boolean setShouldMerge(Boolean shouldMerge)
  {
    return this.shouldMerge = shouldMerge;
  }
  
  @Override
  public IContextInstance getContext()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setContext(IContextInstance context)
  {
    // TODO Auto-generated method stub
    
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
  public Long getIid()
  {
    return iid;
  }
  
  @Override
  public void setIid(Long iid)
  {
    this.iid = iid;
  }
}
