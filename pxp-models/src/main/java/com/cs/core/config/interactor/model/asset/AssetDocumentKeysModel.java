package com.cs.core.config.interactor.model.asset;

import com.cs.core.config.interactor.model.configdetails.AbstractAssetKeysModel;

import java.util.Map;

public class AssetDocumentKeysModel extends AbstractAssetKeysModel
    implements IAssetDocumentKeysModel {
  
  protected String previewKey;
  
  public AssetDocumentKeysModel(String imageKey, String thumbKey, Map<String, Object> metadata,
      String hash, String previewKey, String key, String fileName, String klassId)
  {
    super(imageKey, thumbKey, metadata, hash, key, fileName, klassId);
    this.previewKey = previewKey;
  }
  
  @Override
  public String getPreviewKey()
  {
    return previewKey;
  }
  
  @Override
  public void setPreviewKey(String previewKey)
  {
    this.previewKey = previewKey;
  }
}
