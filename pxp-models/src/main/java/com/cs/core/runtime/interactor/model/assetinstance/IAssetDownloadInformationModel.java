package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAssetDownloadInformationModel extends IModel {
  
  public static String ASSET_INSTANCE_ID     = "assetInstanceId";
  public static String ASSET_INSTANCE_NAME   = "assetInstanceName";
  public static String FILE_NAME_TO_DOWNLOAD = "fileNameToDownload";
  public static String ASSET_FILE_NAME       = "assetFileName";
  public static String EXTENSION             = "extension";
  public static String CAN_DOWNLOAD          = "canDownload";
  public static String SIZE                  = "size";
  public static String ASSET_KLASS_NAME      = "assetKlassName";
  public static String ASSET_OBJECT_KEY      = "assetObjectKey";
  public static String CONTAINER             = "container";
  
  public long getAssetInstanceId();
  public void setAssetInstanceId(long assetInstanceId);
  
  public String getAssetInstanceName();
  public void setAssetInstanceName(String assetInstanceName);
  
  public String getFileNameToDownload();
  public void setFileNameToDownload(String fileNameToDownload);
  
  public String getAssetFileName();
  public void setAssetFileName(String assetFileName);
  
  public String getExtension();
  public void setExtension(String extension);
  
  public boolean getCanDownload();
  public void setCanDownload(boolean canDownload);
  
  public long getSize();
  public void setSize(long size);
  
  public String getAssetKlassName();
  public void setAssetKlassName(String assetKlassName);
  
  public String getAssetObjectKey();
  public void setAssetObjectKey(String assetObjectKey);
  
  public String getContainer();
  public void setContainer(String container);
}
