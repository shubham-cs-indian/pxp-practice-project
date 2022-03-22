package com.cs.core.config.interactor.model.asset;

public interface IAssetDocumentKeysModel extends IAssetKeysModel {
  
  public static final String PREVIEW_KEY = "previewKey";
  
  public String getPreviewKey();
  
  public void setPreviewKey(String previewKey);
}
