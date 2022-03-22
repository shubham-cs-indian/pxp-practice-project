package com.cs.core.runtime.interactor.model.dataintegration;

public interface IInboundTileInfoModel extends ITileInfoModel {
  
  public static final String LABEL                  = "label";
  public static final String SUCCESS                = "success";
  public static final String FAILURE                = "failure";
  public static final String TIMESTAMP              = "timeStamp";
  public static final String ASSETINSTANCEID        = "assetInstanceId";
  public static final String TOTAL_RED_VIOLATION    = "totalRedViolations";
  public static final String TOTAL_ORANGE_VIOLATION = "totalOrangeViolations";
  public static final String TOTAL_YELLOW_VIOLATION = "totalYellowViolations";
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public Integer getSuccess();
  
  public void setSuccess(Integer success);
  
  public Integer getFailure();
  
  public void setFailure(Integer failure);
  
  public Long getTimeStamp();
  
  public void setTimeStamp(Long timeStamp);
  
  public String getAssetInstanceId();
  
  public void setAssetInstanceId(String assetInstanceId);
  
  public Integer getTotalRedViolations();
  
  public void setTotalRedViolations(Integer totalRedViolations);
  
  public Integer getTotalOrangeViolations();
  
  public void setTotalOrangeViolations(Integer totalOrangeViolations);
  
  public Integer getTotalYellowViolations();
  
  public void setTotalYellowViolations(Integer totalYellowViolations);
}
