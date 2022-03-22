package com.cs.core.runtime.interactor.entity.propertyinstance;

import com.cs.core.runtime.interactor.entity.datarule.AttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.datarule.IAttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImageAttributeInstance extends PropertyInstance implements IImageAttributeInstance {
  
  private static final long                    serialVersionUID  = 1L;
  
  protected List<? extends ITagInstanceValue>  tagRelevances;
  protected String                             attributeId;
  protected String                             thumbKey;
  protected String                             assetObjectKey;
  protected HashMap<String, String>            properties;
  protected String                             baseType          = this.getClass()
      .getName();
  protected String                             type;
  protected String                             byteStream;
  protected String                             fileName;
  protected Boolean                            isDefault         = false;
  protected String                             description;
  protected String                             variantOf;
  protected List<ITagInstance>                 tags              = new ArrayList<>();
  protected String                             previewImageKey;
  protected String                             hash;
  protected List<IAttributeConflictingValue>   conflictingValues = new ArrayList<>();
  protected List<IDuplicateTypeAndInstanceIds> duplicateStatus   = new ArrayList<>();
  protected Integer                            isUnique          = -1;
  
  @Override
  public String getThumbKey()
  {
    return this.thumbKey;
  }
  
  @Override
  public void setThumbKey(String imageKey)
  {
    this.thumbKey = imageKey;
  }
  
  @Override
  public String getAssetObjectKey()
  {
    return this.assetObjectKey;
  }
  
  @Override
  public void setAssetObjectKey(String imageOriginalKey)
  {
    this.assetObjectKey = imageOriginalKey;
  }
  
  @Override
  public String getBaseType()
  {
    return this.baseType;
  }
  
  @Override
  public void setBaseType(String type)
  {
    this.baseType = type;
  }
  
  @Override
  public String getType()
  {
    return this.type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getByteStream()
  {
    return this.byteStream;
  }
  
  @Override
  public void setByteStream(String byteStream)
  {
    this.byteStream = byteStream;
  }
  
  @Override
  public String getFileName()
  {
    return this.fileName;
  }
  
  @Override
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
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
  public List<? extends ITagInstance> getTags()
  {
    return this.tags;
  }
  
  @SuppressWarnings("unchecked")
  @JsonDeserialize(contentAs = TagInstance.class)
  @Override
  public void setTags(List<? extends ITagInstance> tags)
  {
    this.tags = (List<ITagInstance>) tags;
  }
  
  @Override
  public HashMap<String, String> getProperties()
  {
    return properties;
  }
  
  @Override
  public void setProperties(HashMap<String, String> properties)
  {
    this.properties = properties;
  }
  
  public String getVariantOf()
  {
    return variantOf;
  }
  
  public void setVariantOf(String variantOf)
  {
    this.variantOf = variantOf;
  }
  
  @Override
  public String getAttributeId()
  {
    return this.attributeId;
  }
  
  @Override
  public void setAttributeId(String attributeId)
  {
    this.attributeId = attributeId;
  }
  
  @Override
  public String getPreviewImageKey()
  {
    return previewImageKey;
  }
  
  @Override
  public void setPreviewImageKey(String previewImageKey)
  {
    this.previewImageKey = previewImageKey;
  }
  
  @Override
  public String getHash()
  {
    return hash;
  }
  
  @Override
  public void setHash(String hash)
  {
    this.hash = hash;
  }
  
  @Override
  public List<IAttributeConflictingValue> getConflictingValues()
  {
    return conflictingValues;
  }
  
  @Override
  @JsonDeserialize(contentAs = AttributeConflictingValue.class)
  public void setConflictingValues(List<IAttributeConflictingValue> conflictingValues)
  {
    this.conflictingValues = conflictingValues;
  }
  
  @Override
  public List<IDuplicateTypeAndInstanceIds> getDuplicateStatus()
  {
    return duplicateStatus;
  }
  
  @Override
  @JsonDeserialize(contentAs = DuplicateTypeAndInstanceIds.class)
  public void setDuplicateStatus(List<IDuplicateTypeAndInstanceIds> duplicateStatus)
  {
    this.duplicateStatus = duplicateStatus;
  }
  
  @Override
  public Integer getIsUnique()
  {
    return isUnique;
  }
  
  @Override
  public void setIsUnique(Integer isUnique)
  {
    this.isUnique = isUnique;
  }
}
