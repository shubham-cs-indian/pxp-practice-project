package com.cs.core.config.interactor.model.downloadtracker;

import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetDownloadLogsFilterDataResponseModel extends IModel {

  public static final String DOWNLOAD_LOG_FILTER_DATA_LIST = "downloadLogFilterDataList";
  public static final String COUNT                         = "count";

  public Map<String, Object> getDownloadLogFilterDataList();
  public void setDownloadLogFilterDataList(Map<String, Object> downloadLogFilterDataList);

  public Long getCount();
  public void setCount(Long count);

}
