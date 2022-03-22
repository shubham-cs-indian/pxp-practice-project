package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAssetDownloadModel extends IModel {
  
  public static final String ID                          = "id";
  public static final String TOTAL_DOWNLOAD_COUNT        = "totalDownloadCount";
  public static final String DOWNLOAD_COUNT_WITHIN_RANGE = "downloadCountWithinRange";
  
  public Long getTotalDownloadCount();
  public void setTotalDownloadCount(Long totalDownloadCount);
  
  public Long getDownloadCountWithinRange();
  public void setDownloadCountWithinRange(Long downloadCountWithinRange);
  
  public String getId();
  public void setId(String id);  
}
