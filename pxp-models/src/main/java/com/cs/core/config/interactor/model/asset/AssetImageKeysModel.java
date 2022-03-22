package com.cs.core.config.interactor.model.asset;

import com.cs.core.config.interactor.model.configdetails.AbstractAssetKeysModel;

import java.util.Map;

public class AssetImageKeysModel extends AbstractAssetKeysModel implements IAssetImageKeysModel {
  
  private static final long serialVersionUID = 1L;
  protected Integer         height;
  protected Integer         width;
  protected String          thumbnailPath;
  protected String          filePath;
  
  public AssetImageKeysModel(String imageKey, String thumbKey, Map<String, Object> metadata,
      String hash, Integer height, Integer width, String key, String fileName, String klassId, String thumbnailPath, String filePath)
  {
    super(imageKey, thumbKey, metadata, hash, key, fileName, klassId);
    this.height = height;
    this.width = width;
    this.thumbnailPath = thumbnailPath;
    this.filePath = filePath;
  }
  
  @Override
  public Integer getHeight()
  {
    return height;
  }
  
  @Override
  public void setHeight(Integer height)
  {
    this.height = height;
  }
  
  @Override
  public Integer getWidth()
  {
    return width;
  }
  
  @Override
  public void setWidth(Integer width)
  {
    this.width = width;
  }

  @Override
  public String getThumbnailPath()
  {
    return thumbnailPath;
  }

  @Override
  public void setThumbnailPath(String thumbnailPath)
  {
    this.thumbnailPath = thumbnailPath;
  }
  
  @Override
  public String getFilePath()
  {
    return filePath;
  }

  @Override
  public void setFilePath(String filePath)
  {
    this.filePath = filePath;
  }
}
