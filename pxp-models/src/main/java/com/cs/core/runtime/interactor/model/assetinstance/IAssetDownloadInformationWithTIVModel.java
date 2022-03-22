package com.cs.core.runtime.interactor.model.assetinstance;

import java.util.List;
import java.util.Map;

public interface IAssetDownloadInformationWithTIVModel extends IAssetDownloadInformationModel {
  
  public static String TOTAL_SIZE               = "totalSize";
  public static String THUMB_KEY                = "thumbKey";
  public static String TOTAL_CONTENT            = "totalContent";
  public static String ALL_DOWNLOAD_PERMISSION  = "allDownloadPermission";
  public static String TIV_DOWNLOAD_INFORMATION = "tivDownloadInformation";
  public static String CLASSIFIER_CODE          = "classifierCode";
  
  public long getTotalSize();
  public void setTotalSize(long totalSize);
  
  public String getThumbKey();
  public void setThumbKey(String thumbKey);
  
  public long getTotalContent();
  public void setTotalContent(long totalContent);
  
  public boolean getAllDownloadPermission();
  public void setAllDownloadPermission(boolean allDownloadPermission);
  
  public Map<String, List<IAssetDownloadInformationModel>> getTIVDownloadInformation();
  public void setTIVDownloadInformation(Map<String, List<IAssetDownloadInformationModel>> tivDownloadInformation);
  
  public String getClassifierCode();
  public void setClassifierCode(String classifierCode);
}
