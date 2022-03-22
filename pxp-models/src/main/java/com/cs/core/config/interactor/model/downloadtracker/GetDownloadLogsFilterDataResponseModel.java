package com.cs.core.config.interactor.model.downloadtracker;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetDownloadLogsFilterDataResponseModel implements IGetDownloadLogsFilterDataResponseModel {

  private static final long     serialVersionUID          = 1L;
  protected Map<String, Object> downloadLogFilterDataList = new HashMap<String, Object>();
  protected Long                count;

  @Override
  public Map<String, Object> getDownloadLogFilterDataList()
  {
    return downloadLogFilterDataList;
  }

  @Override
  @JsonDeserialize(contentAs = String.class)
  public void setDownloadLogFilterDataList(Map<String, Object> downloadLogFilterDataList)
  {
    this.downloadLogFilterDataList = downloadLogFilterDataList;
  }

  @Override
  public Long getCount()
  {
    return count;
  }

  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
}
