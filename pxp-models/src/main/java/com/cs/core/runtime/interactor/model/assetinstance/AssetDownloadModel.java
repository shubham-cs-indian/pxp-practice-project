package com.cs.core.runtime.interactor.model.assetinstance;

public class AssetDownloadModel implements IAssetDownloadModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected Long            totalDownloadCount;
  protected Long            downloadCountWithinRange;
  
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
  public Long getTotalDownloadCount()
  {
    return totalDownloadCount;
  }
  
  @Override
  public void setTotalDownloadCount(Long totalCount)
  {
    this.totalDownloadCount = totalCount;
  }
  
  @Override
  public Long getDownloadCountWithinRange()
  {
    return downloadCountWithinRange;
  }

  @Override
  public void setDownloadCountWithinRange(Long rangeCount)
  {
    this.downloadCountWithinRange = rangeCount;
  }
  
}
