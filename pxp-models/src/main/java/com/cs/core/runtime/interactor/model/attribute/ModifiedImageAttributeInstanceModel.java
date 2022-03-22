package com.cs.core.runtime.interactor.model.attribute;

import com.cs.core.runtime.interactor.entity.propertyinstance.IImageAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ImageAttributeInstance;
import com.cs.core.runtime.interactor.model.instance.IModifiedImageAttributeInstanceModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.HashMap;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, defaultImpl = ModifiedImageAttributeInstanceModel.class)
public class ModifiedImageAttributeInstanceModel extends
    AbstractModifiedContentAttributeInstanceModel implements IModifiedImageAttributeInstanceModel {
  
  private static final long serialVersionUID = 1L;
  protected String          sourceId;
  protected Boolean         isConflictResolved;
  protected Long            iid;
  
  public ModifiedImageAttributeInstanceModel(IImageAttributeInstance attributeInstance)
  {
    super(attributeInstance);
  }
  
  public ModifiedImageAttributeInstanceModel()
  {
    super(new ImageAttributeInstance());
  }
  
  @Override
  public String getThumbKey()
  {
    return ((IImageAttributeInstance) entity).getThumbKey();
  }
  
  @Override
  public void setThumbKey(String imageKey)
  {
    ((IImageAttributeInstance) entity).setThumbKey(imageKey);
  }
  
  @Override
  public String getAssetObjectKey()
  {
    return ((IImageAttributeInstance) entity).getAssetObjectKey();
  }
  
  @Override
  public void setAssetObjectKey(String assetObjectKey)
  {
    ((IImageAttributeInstance) entity).setAssetObjectKey(assetObjectKey);
  }
  
  @Override
  public HashMap<String, String> getProperties()
  {
    return ((IImageAttributeInstance) entity).getProperties();
  }
  
  @Override
  public void setProperties(HashMap<String, String> properties)
  {
    ((IImageAttributeInstance) entity).setProperties(properties);
  }
  
  @Override
  public String getByteStream()
  {
    return ((IImageAttributeInstance) entity).getByteStream();
  }
  
  @Override
  public void setByteStream(String byteStream)
  {
    ((IImageAttributeInstance) entity).setByteStream(byteStream);
  }
  
  @Override
  public String getFileName()
  {
    return ((IImageAttributeInstance) entity).getFileName();
  }
  
  @Override
  public void setFileName(String fileName)
  {
    ((IImageAttributeInstance) entity).setFileName(fileName);
  }
  
  @Override
  public Boolean getIsDefault()
  {
    return ((IImageAttributeInstance) entity).getIsDefault();
  }
  
  @Override
  public void setIsDefault(Boolean isDefault)
  {
    ((IImageAttributeInstance) entity).setIsDefault(isDefault);
  }
  
  @Override
  public String getDescription()
  {
    return ((IImageAttributeInstance) entity).getDescription();
  }
  
  @Override
  public void setDescription(String description)
  {
    ((IImageAttributeInstance) entity).setDescription(description);
  }
  
  @Override
  public String getType()
  {
    return ((IImageAttributeInstance) entity).getType();
  }
  
  @Override
  public void setType(String type)
  {
    ((IImageAttributeInstance) entity).setType(type);
  }
  
  @Override
  public String getAttributeId()
  {
    return ((IImageAttributeInstance) entity).getAttributeId();
  }
  
  @Override
  public void setAttributeId(String attributeId)
  {
    ((IImageAttributeInstance) entity).setAttributeId(attributeId);
  }
  
  @Override
  public String getPreviewImageKey()
  {
    return ((IImageAttributeInstance) entity).getPreviewImageKey();
  }
  
  @Override
  public void setPreviewImageKey(String previewImageKey)
  {
    ((IImageAttributeInstance) entity).setPreviewImageKey(previewImageKey);
  }
  
  @Override
  public Map<String, Object> getNotification()
  {
    return ((IImageAttributeInstance) entity).getNotification();
  }
  
  @Override
  public void setNotification(Map<String, Object> notification)
  {
    ((IImageAttributeInstance) entity).setNotification(notification);
  }
  
  @Override
  public String getHash()
  {
    return ((IImageAttributeInstance) entity).getHash();
  }
  
  @Override
  public void setHash(String hash)
  {
    ((IImageAttributeInstance) entity).setHash(hash);
  }
  
  @Override
  public Boolean getIsConflictResolved()
  {
    if (isConflictResolved == null) {
      isConflictResolved = true;
    }
    return isConflictResolved;
  }
  
  @Override
  public void setIsConflictResolved(Boolean isConflictResolved)
  {
    this.isConflictResolved = isConflictResolved;
  }
  
  @Override
  public String getSourceId()
  {
    return sourceId;
  }
  
  @Override
  public void setSourceId(String sourceId)
  {
    this.sourceId = sourceId;
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
