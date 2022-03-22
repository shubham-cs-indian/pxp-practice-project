package com.cs.core.config.interactor.model.downloadtracker;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetDownloadLogListResponseModel extends IModel {
  
  public static final String FROM              = "from";
  public static final String SIZE              = "size";
  public static final String TOTAL_COUNT       = "totalCount";
  public static final String DOWNLOAD_LOG_LIST = "downloadLogList";
  
  public Long getFrom();
  public void setFrom(Long from);
  
  public Long getSize();
  public void setSize(Long size);
  
  public Long getTotalCount();
  public void setTotalCount(Long totalCount);
  
  public List<IGetDownloadLogsModel> getDownloadLogList();
  public void setDownloadLogList(List<IGetDownloadLogsModel> downloadLogList);
 
}
