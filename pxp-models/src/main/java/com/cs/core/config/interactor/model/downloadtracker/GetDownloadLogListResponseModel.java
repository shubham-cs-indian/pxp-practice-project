package com.cs.core.config.interactor.model.downloadtracker;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetDownloadLogListResponseModel implements IGetDownloadLogListResponseModel {
  
  private static final long             serialVersionUID = 1L;
  protected Long                        from;
  protected Long                        size;
  protected Long                        totalCount;
  protected List<IGetDownloadLogsModel> downloadLogList  = new ArrayList<>();
  
  @Override
  public Long getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Long from)
  {
    this.from = from;
  }
  
  @Override
  public Long getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Long size)
  {
    this.size = size;
  }
  
  @Override
  public Long getTotalCount()
  {
    return totalCount;
  }
  
  @Override
  public void setTotalCount(Long totalCount)
  {
    this.totalCount = totalCount;
  }
  
  @Override
  public List<IGetDownloadLogsModel> getDownloadLogList()
  {
    return downloadLogList;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetDownloadLogsModel.class)
  public void setDownloadLogList(List<IGetDownloadLogsModel> downloadLogList)
  {
    this.downloadLogList = downloadLogList;
  }
  
}
