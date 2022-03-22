package com.cs.core.runtime.interactor.model.dataintegration;

public class InboundSummaryTileInfoModel extends TileInfoModel
    implements IInboundSummaryTileInfoModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Integer         totalFileUploads;
  protected Integer         successfulUploads;
  protected Integer         failedImports;
  protected Integer         successfulImports;
  protected Integer         totalRedViolations;
  protected Integer         totalOrangeViolations;
  protected Integer         totalYellowViolations;
  
  @Override
  public Integer getTotalFileUploads()
  {
    return totalFileUploads;
  }
  
  @Override
  public void setTotalFileUploads(Integer totalFileUploads)
  {
    this.totalFileUploads = totalFileUploads;
  }
  
  @Override
  public Integer getSuccessfulUploads()
  {
    return successfulUploads;
  }
  
  @Override
  public void setSuccessfulUploads(Integer successfulUploads)
  {
    this.successfulUploads = successfulUploads;
  }
  
  @Override
  public Integer getFailedImports()
  {
    return failedImports;
  }
  
  @Override
  public void setFailedImports(Integer failedImports)
  {
    this.failedImports = failedImports;
  }
  
  @Override
  public Integer getSuccessfulImports()
  {
    return successfulImports;
  }
  
  @Override
  public void setSuccessfulImports(Integer successfulImports)
  {
    this.successfulImports = successfulImports;
  }
  
  @Override
  public Integer getTotalRedViolations()
  {
    return totalRedViolations;
  }
  
  @Override
  public void setTotalRedViolations(Integer totalRedViolations)
  {
    this.totalRedViolations = totalRedViolations;
  }
  
  @Override
  public Integer getTotalOrangeViolations()
  {
    return totalOrangeViolations;
  }
  
  @Override
  public void setTotalOrangeViolations(Integer totalOrangeViolations)
  {
    this.totalOrangeViolations = totalOrangeViolations;
  }
  
  @Override
  public Integer getTotalYellowViolations()
  {
    return totalYellowViolations;
  }
  
  @Override
  public void setTotalYellowViolations(Integer totalYellowViolations)
  {
    this.totalYellowViolations = totalYellowViolations;
  }
}
