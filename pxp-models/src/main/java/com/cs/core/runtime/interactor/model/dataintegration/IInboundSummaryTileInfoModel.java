package com.cs.core.runtime.interactor.model.dataintegration;

public interface IInboundSummaryTileInfoModel extends ITileInfoModel {
  
  public static final String TOTAL_FILE_UPLOADS      = "totalFileUploads";
  public static final String SUCCESSFUL_UPLOADS      = "successfulUploads";
  public static final String FAILED_IMPORTS          = "failedImports";
  public static final String SUCCESSFUL_IMPORTS      = "successfulImports";
  public static final String TOTAL_RED_VIOLATIONS    = "totalRedViolations";
  public static final String TOTAL_ORANGE_VIOLATIONS = "totalOrangeViolations";
  public static final String TOTAL_YELLOW_VIOLATIONS = "totalYellowViolations";
  
  public Integer getTotalFileUploads();
  
  public void setTotalFileUploads(Integer totalFileUploads);
  
  public Integer getSuccessfulUploads();
  
  public void setSuccessfulUploads(Integer successfulUploads);
  
  public Integer getFailedImports();
  
  public void setFailedImports(Integer failedImports);
  
  public Integer getSuccessfulImports();
  
  public void setSuccessfulImports(Integer successfulImports);
  
  public Integer getTotalRedViolations();
  
  public void setTotalRedViolations(Integer totalRedViolations);
  
  public Integer getTotalOrangeViolations();
  
  public void setTotalOrangeViolations(Integer totalOrangeViolations);
  
  public Integer getTotalYellowViolations();
  
  public void setTotalYellowViolations(Integer totalYellowViolations);
}
