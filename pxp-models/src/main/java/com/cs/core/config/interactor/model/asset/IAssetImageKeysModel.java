package com.cs.core.config.interactor.model.asset;

public interface IAssetImageKeysModel extends IAssetKeysModel {
  
  public static final String HEIGHT         = "height";
  public static final String WIDTH          = "width";
  public static final String THUMBNAIL_PATH = "thumbnailPath";
  public static final String FILE_PATH      = "filePath";
  
  public Integer getHeight();
  public void setHeight(Integer height);
  
  public Integer getWidth();
  public void setWidth(Integer width);
  
  public String getThumbnailPath();
  public void setThumbnailPath(String thumbnailPath);
  
  public String getFilePath();
  public void setFilePath(String filePath);
}
