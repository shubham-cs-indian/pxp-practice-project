package com.cs.core.runtime.interactor.model.assetinstance;


public class AssetDownloadInformationModel implements IAssetDownloadInformationModel {
  
  private static final long serialVersionUID = 1L;
  private long              assetInstanceId  = 0;
  private String            assetInstanceName;
  private String            assetFileName;
  private String            fileNameToDownload;
  private String            extension;
  private boolean           canDownload      = false;
  private long              size             = 0;
  private String            assetKlassName;
  private String            assetObjectKey;
  private String            container;
  
  @Override
  public long getAssetInstanceId()
  {
    return assetInstanceId;
  }
  
  @Override
  public void setAssetInstanceId(long assetInstanceId)
  {
    this.assetInstanceId = assetInstanceId;
  }
  
  @Override
  public String getAssetInstanceName()
  {
    return assetInstanceName;
  }

  @Override
  public void setAssetInstanceName(String assetInstanceName)
  {
    this.assetInstanceName = assetInstanceName;
  }

  @Override
  public String getFileNameToDownload()
  {
    return fileNameToDownload;
  }
  
  @Override
  public void setFileNameToDownload(String fileNameToDownload)
  {
    this.fileNameToDownload = fileNameToDownload;
  }
  
  @Override
  public String getAssetFileName()
  {
    return assetFileName;
  }
  
  @Override
  public void setAssetFileName(String assetFileName)
  {
    this.assetFileName = assetFileName;
  }
  
  @Override
  public String getExtension()
  {
    return extension;
  }
  
  @Override
  public void setExtension(String extension)
  {
    this.extension = extension;
  }
  
  @Override
  public boolean getCanDownload()
  {
    return canDownload;
  }
  
  @Override
  public void setCanDownload(boolean canDownload)
  {
    this.canDownload = canDownload;
  }
  
  @Override
  public long getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(long size)
  {
    this.size = size;
  }

  @Override
  public String getAssetKlassName()
  {
    return assetKlassName;
  }

  @Override
  public void setAssetKlassName(String assetKlassName)
  {
    this.assetKlassName = assetKlassName;
  }
  
  @Override
  public String getAssetObjectKey()
  {
    return assetObjectKey;
  }

  @Override
  public void setAssetObjectKey(String assetObjectKey)
  {
    this.assetObjectKey = assetObjectKey;
  }
  
  @Override
  public String getContainer()
  {
    return container;
  }

  @Override
  public void setContainer(String container)
  {
    this.container = container;
  }
}
