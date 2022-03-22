package com.cs.core.runtime.interactor.model.assetinstance;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.entity.eventinstance.EventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.entity.klassinstance.AssetInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IAssetInstance;
import com.cs.core.runtime.interactor.model.instance.AbstractContentInstanceSaveModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedAssetAttributeInstanceModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = Id.NONE)
public class AssetInstanceSaveModel extends AbstractContentInstanceSaveModel
    implements IAssetInstanceSaveModel {
  
  private static final long              serialVersionUID      = 1L;
  
  protected Map<String, Object>          metadata;
  
  protected IEventInstanceSchedule       modifiedEventSchedule;
  
  protected List<IEventInstanceSchedule> addedInclusions;
  protected List<IEventInstanceSchedule> deletedInclusions;
  protected List<IEventInstanceSchedule> modifiedInclusions;
  
  protected List<IEventInstanceSchedule> addedExclusions;
  protected List<IEventInstanceSchedule> deletedExclusions;
  protected List<IEventInstanceSchedule> modifiedExclusions;
  protected boolean                      triggerAfterSaveEvent = true;
  protected Long                         iid;
  
  protected boolean                      isResetDownloadCount  = false;
  private long                           duplicateId           = 0;
  protected String                       thumbnailPath;
  protected String                       filePath;
  protected boolean                      isFileUploaded        = false;
  
  public AssetInstanceSaveModel()
  {
    this.entity = new AssetInstance();
  }
  
  public AssetInstanceSaveModel(IAssetInstance assetInstance)
  {
    this.entity = assetInstance;
  }
  
  @Override
  public Map<String, Object> getMetadata()
  {
    return metadata;
  }
  
  @Override
  public void setMetadata(Map<String, Object> metadata)
  {
    this.metadata = metadata;
  }
  
  @Override
  public List<? extends IModifiedAssetAttributeInstanceModel> getModifiedAssets()
  {
    return modifiedAssets;
  }
  
  // @JsonIgnore
  @JsonDeserialize(contentAs = ModifiedAssetAttributeInstanceModel.class)
  @Override
  public void setModifiedAssets(List<? extends IModifiedAssetAttributeInstanceModel> modifiedAssets)
  {
    this.modifiedAssets = modifiedAssets;
  }
  
  @Override
  public IEventInstanceSchedule getModifiedEventSchedule()
  {
    return modifiedEventSchedule;
  }
  
  @JsonDeserialize(as = EventInstanceSchedule.class)
  @Override
  public void setModifiedEventSchedule(IEventInstanceSchedule modifiedEventSchedule)
  {
    this.modifiedEventSchedule = modifiedEventSchedule;
  }
  
  @Override
  public List<IEventInstanceSchedule> getAddedInclusions()
  {
    return addedInclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setAddedInclusions(List<IEventInstanceSchedule> addedInclusions)
  {
    this.addedInclusions = addedInclusions;
  }
  
  @Override
  public List<IEventInstanceSchedule> getDeletedInclusions()
  {
    return deletedInclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setDeletedInclusions(List<IEventInstanceSchedule> deletedInclusions)
  {
    this.deletedInclusions = deletedInclusions;
  }
  
  @Override
  public List<IEventInstanceSchedule> getModifiedInclusions()
  {
    return modifiedInclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setModifiedInclusions(List<IEventInstanceSchedule> modifiedInclusions)
  {
    this.modifiedInclusions = modifiedInclusions;
  }
  
  @Override
  public List<IEventInstanceSchedule> getAddedExclusions()
  {
    return addedExclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setAddedExclusions(List<IEventInstanceSchedule> addedExclusions)
  {
    this.addedExclusions = addedExclusions;
  }
  
  @Override
  public List<IEventInstanceSchedule> getDeletedExclusions()
  {
    return deletedExclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setDeletedExclusions(List<IEventInstanceSchedule> deletedExclusions)
  {
    this.deletedExclusions = deletedExclusions;
  }
  
  @Override
  public List<IEventInstanceSchedule> getModifiedExclusions()
  {
    return modifiedExclusions;
  }
  
  @JsonDeserialize(contentAs = EventInstanceSchedule.class)
  @Override
  public void setModifiedExclusions(List<IEventInstanceSchedule> modifiedExclusions)
  {
    this.modifiedExclusions = modifiedExclusions;
  }
  
  @Override
  @JsonIgnore
  public IEventInstanceSchedule getEventSchedule()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IAssetInformationModel getAssetInformation()
  {
    return ((AssetInstance) this.entity).getAssetInformation(); 
  }
  
  @JsonDeserialize(as = AssetInformationModel.class)
  @Override
  public void setAssetInformation(IAssetInformationModel assetInformation)
  {
    ((AssetInstance) this.entity).setAssetInformation(assetInformation);
  }
  
  /**
   * *************** ignored fields ******************
   */
  @Override
  @JsonIgnore
  public void setEventSchedule(IEventInstanceSchedule schedule)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public boolean getTriggerEvent()
  {
    return this.triggerAfterSaveEvent;
  }
  
  @Override
  public void setTriggerEvent(boolean triggerEvent)
  {
    this.triggerAfterSaveEvent = triggerEvent;
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

  @Override
  public boolean getIsExpired()
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void setIsExpired(boolean isExpired)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean getIsResetDownloadCount()
  {
    return this.isResetDownloadCount;
  }

  @Override
  public void setIsResetDownloadCount(boolean isResetDownloadCount)
  {
    this.isResetDownloadCount = isResetDownloadCount;
  }
  
  @Override
  public long getDuplicateId()
  {
    return duplicateId;
  }
  
  @Override
  public void setDuplicateId(long duplicateId)
  {
    this.duplicateId = duplicateId;
  }
  
  @Override
  public String getThumbnailPath()
  {
    return thumbnailPath;
  }

  @Override
  public void setThumbnailPath(String thumbnailPath)
  {
    this.thumbnailPath = thumbnailPath;
  }
  
  @Override
  public String getFilePath()
  {
    return filePath;
  }

  @Override
  public void setFilePath(String filePath)
  {
    this.filePath = filePath;
  }
  
  @Override
  public boolean getIsFileUploaded()
  {
    return this.isFileUploaded;
  }

  @Override
  public void setIsFileUploaded(boolean isFileUploaded)
  {
    this.isFileUploaded = isFileUploaded;
  }
}
