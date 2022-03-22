package com.cs.core.runtime.interactor.model.dataintegration;

public class InboundTileInfoModel extends TileInfoModel implements IInboundTileInfoModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          label;
  protected Integer         success;
  protected Integer         failure;
  protected Long            timeStamp;
  protected String          assetInstanceId;
  protected Integer         totalRedViolations;
  protected Integer         totalOrangeViolations;
  protected Integer         totalYellowViolations;
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public Integer getSuccess()
  {
    return success;
  }
  
  @Override
  public void setSuccess(Integer success)
  {
    this.success = success;
  }
  
  @Override
  public Integer getFailure()
  {
    return failure;
  }
  
  @Override
  public void setFailure(Integer failure)
  {
    this.failure = failure;
  }
  
  @Override
  public Long getTimeStamp()
  {
    return timeStamp;
  }
  
  @Override
  public void setTimeStamp(Long timeStamp)
  {
    this.timeStamp = timeStamp;
  }
  
  @Override
  public String getAssetInstanceId()
  {
    return this.assetInstanceId;
  }
  
  @Override
  public void setAssetInstanceId(String assetInstanceId)
  {
    this.assetInstanceId = assetInstanceId;
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
