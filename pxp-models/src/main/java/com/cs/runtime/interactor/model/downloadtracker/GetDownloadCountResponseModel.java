package com.cs.runtime.interactor.model.downloadtracker;


public class GetDownloadCountResponseModel implements IGetDownloadCountResponseModel {
  
  private static final long serialVersionUID = 1L;
  protected Long            totalDownloadCount;
  protected Long            downloadCountWithinRange;
  
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
