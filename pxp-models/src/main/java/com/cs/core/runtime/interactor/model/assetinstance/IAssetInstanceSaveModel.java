package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.entity.eventinstance.IEventInstanceSchedule;
import com.cs.core.runtime.interactor.model.instance.IContentInstanceSaveModel;

import java.util.List;
import java.util.Map;

public interface IAssetInstanceSaveModel extends IAssetInstanceModel, IContentInstanceSaveModel {
  
  /* public static final String ADDED_ASSETS   = "addedAssets";
  public static final String DELETED_ASSETS = "deletedAssets";*/
  public static final String METADATA                = "metadata";
  
  public static final String MODIFIED_EVENT_SCHEDULE = "modifiedEventSchedule";
  
  public static final String ADDED_INCLUSIONS        = "addedInclusions";
  public static final String DELETED_INCLUSIONS      = "deletedInclusions";
  public static final String MODIFIED_INCLUSIONS     = "modifiedInclusions";
  
  public static final String ADDED_EXCLUSIONS        = "addedExclusions";
  public static final String DELETED_EXCLUSIONS      = "deletedExclusions";
  public static final String MODIFIED_EXCLUSIONS     = "modifiedExclusions";

  public static final String IS_RESET_DOWNLOAD_COUNT = "isResetDownloadCount";
  public static final String DUPLICATE_ID            = "duplicateId";
  public static final String THUMBNAIL_PATH          = "thumbnailPath";
  public static final String FILE_PATH               = "filePath";
  public static final String IS_FILE_UPLOADED           = "isFileUploaded";
  
  /*
  public void setAddedAssets(List<? extends IKlassInstanceImage> addedAssets);
  public List<? extends IKlassInstanceImage> getAddedAssets();
  
  public List<? extends IKlassInstanceImage> getDeletedAssets();
  public void setDeletedAssets(List<? extends IKlassInstanceImage> deletedAssets);*/
  
  public Map<String, Object> getMetadata();
  
  public void setMetadata(Map<String, Object> metadata);
  
  public IEventInstanceSchedule getModifiedEventSchedule();
  
  public void setModifiedEventSchedule(IEventInstanceSchedule modifiedEventSchedule);
  
  public List<IEventInstanceSchedule> getAddedInclusions();
  
  public void setAddedInclusions(List<IEventInstanceSchedule> addedInclusions);
  
  public List<IEventInstanceSchedule> getDeletedInclusions();
  
  public void setDeletedInclusions(List<IEventInstanceSchedule> deletedInclusions);
  
  public List<IEventInstanceSchedule> getModifiedInclusions();
  
  public void setModifiedInclusions(List<IEventInstanceSchedule> modifiedInclusions);
  
  public List<IEventInstanceSchedule> getAddedExclusions();
  
  public void setAddedExclusions(List<IEventInstanceSchedule> addedExclusions);
  
  public List<IEventInstanceSchedule> getDeletedExclusions();
  
  public void setDeletedExclusions(List<IEventInstanceSchedule> deletedExclusions);
  
  public List<IEventInstanceSchedule> getModifiedExclusions();
  
  public void setModifiedExclusions(List<IEventInstanceSchedule> modifiedExclusions);
  
  public boolean getTriggerEvent();
  
  public void setTriggerEvent(boolean triggerEvent);
  
  public boolean getIsResetDownloadCount();
  public void setIsResetDownloadCount(boolean isResetDownloadCount);
  
  public long getDuplicateId();
  public void setDuplicateId(long duplicateId);
  
  public String getThumbnailPath();
  public void setThumbnailPath(String thumbnailPath);

  public String getFilePath();
  public void setFilePath(String filePath);
  
  public boolean getIsFileUploaded();
  public void setIsFileUploaded(boolean isFileUploaded);
}
