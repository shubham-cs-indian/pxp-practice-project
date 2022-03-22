package com.cs.runtime.interactor.model.downloadtracker;

import com.cs.core.runtime.interactor.model.configuration.IModel;


public interface IGetDownloadCountResponseModel extends IModel {
  
  public static final String TOTAL_DOWNLOAD_COUNT        = "totalDownloadCount";
  public static final String DOWNLOAD_COUNT_WITHIN_RANGE = "downloadCountWithinRange";
  
  public Long getTotalDownloadCount();
  public void setTotalDownloadCount(Long totalDownloadCount);
  
  public Long getDownloadCountWithinRange();
  public void setDownloadCountWithinRange(Long downloadCountWithinRange);
}
