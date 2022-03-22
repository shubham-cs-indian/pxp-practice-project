package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.model.asset.AssetExtensionConfiguration;
import com.cs.core.config.interactor.model.asset.IAssetExtensionConfiguration;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class Asset extends AbstractKlass implements IAsset {
  
  private static final long                    serialVersionUID                        = 1L;
  protected Boolean                            detectDuplicate                         = false;
  protected Boolean                            isDetectDuplicateModified               = false;
  protected Boolean                            uploadZip                               = false;
  protected List<IAssetExtensionConfiguration> extensionConfiguration                  = new ArrayList<IAssetExtensionConfiguration>();
  protected Boolean                            indesignServer                          = false;
  protected boolean                            trackDownloads                          = false;
  protected boolean                            shouldDownloadAssetWithOriginalFilename = false;

  @JsonDeserialize(as = Asset.class)
  @Override
  public void setParent(ITreeEntity parent)
  {
    this.parent = (Asset) parent;
  }
  
  @Override
  public Boolean getDetectDuplicate()
  {
    return detectDuplicate;
  }
  
  @Override
  public void setDetectDuplicate(Boolean detectDuplicate)
  {
    this.detectDuplicate = detectDuplicate;
  }
  
  @Override
  public Boolean getUploadZip()
  {
    return uploadZip;
  }
  
  @Override
  public void setUploadZip(Boolean uploadZip)
  {
    this.uploadZip = uploadZip;
  }
  
  @Override
  public List<IAssetExtensionConfiguration> getExtensionConfiguration()
  {
    return extensionConfiguration;
  }
  
  @Override
  @JsonDeserialize(contentAs = AssetExtensionConfiguration.class)
  public void setExtensionConfiguration(List<IAssetExtensionConfiguration> extensionConfiguration)
  {
    this.extensionConfiguration = extensionConfiguration;
  }
  
  @Override
  public Boolean getIndesignServer()
  {
    return indesignServer;
  }
  
  @Override
  public void setIndesignServer(Boolean indesignServer)
  {
    this.indesignServer = indesignServer;
  }
  
  @Override
  public boolean getTrackDownloads()
  {
    return this.trackDownloads;
  }

  @Override
  public void setTrackDownloads(boolean trackDownloads)
  {
    this.trackDownloads = trackDownloads;
  }

  @Override
  public Boolean getIsDetectDuplicateModified()
  {
    return isDetectDuplicateModified;
  }

  @Override
  public void setIsDetectDuplicateModified(Boolean isDetectDuplicateModified)
  {
    this.isDetectDuplicateModified = isDetectDuplicateModified;    
  }

  @Override
  public boolean isShouldDownloadAssetWithOriginalFilename()
  {
    return shouldDownloadAssetWithOriginalFilename;
  }

  @Override
  public void setShouldDownloadAssetWithOriginalFilename(
      boolean shouldDownloadAssetWithOriginalFilename)
  {
    this.shouldDownloadAssetWithOriginalFilename = shouldDownloadAssetWithOriginalFilename;
  }
  
  

}
