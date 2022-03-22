package com.cs.core.runtime.interactor.model.assetinstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.model.asset.BulkAssetDownloadWithTIVsCustomDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


public class AssetDownloadInformationWithTIVModel extends AssetDownloadInformationModel
    implements IAssetDownloadInformationWithTIVModel {
  
  private static final long serialVersionUID = 1L;
  private long                                              totalSize              = 0;
  private String                                            thumbKey;
  private long                                              totalContent           = 0;
  private boolean                                           allDownloadPermission  = false;
  private Map<String, List<IAssetDownloadInformationModel>> tivDownloadInformation = new HashMap<>();
  private String                                            classifierCode;
  
  @Override
  public long getTotalSize()
  {
    return totalSize;
  }
  
  @Override
  public void setTotalSize(long totalSize)
  {
    this.totalSize = totalSize;
  }
  
  @Override
  public String getThumbKey()
  {
    return thumbKey;
  }
  
  @Override
  public void setThumbKey(String thumbKey)
  {
    this.thumbKey = thumbKey;
  }
  
  @Override
  public long getTotalContent()
  {
    return totalContent;
  }
  
  @Override
  public void setTotalContent(long totalContent)
  {
    this.totalContent = totalContent;
  }
  
  @Override
  public boolean getAllDownloadPermission()
  {
    return allDownloadPermission;
  }
  
  @Override
  public void setAllDownloadPermission(boolean allDownloadPermission)
  {
    this.allDownloadPermission = allDownloadPermission;
  }
  
  @Override
  public Map<String, List<IAssetDownloadInformationModel>> getTIVDownloadInformation()
  {
    return tivDownloadInformation;
  }
  
  @JsonDeserialize(contentUsing = BulkAssetDownloadWithTIVsCustomDeserializer.class)
  @Override
  public void setTIVDownloadInformation(
      Map<String, List<IAssetDownloadInformationModel>> tivDownloadInformation)
  {
    this.tivDownloadInformation = tivDownloadInformation;
  }

  @Override
  public String getClassifierCode()
  {
    return classifierCode;
  }

  @Override
  public void setClassifierCode(String classifierCode)
  {
    this.classifierCode = classifierCode;
  }
}
