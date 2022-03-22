package com.cs.core.config.interactor.model.asset;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface IAssetExtensionConfiguration extends IConfigEntity {
  
  public static final String EXTRACT_METADATA  = "extractMetadata";
  public static final String EXTRACT_RENDITION = "extractRendition";
  public static final String EXTENSION         = "extension";
  
  public Boolean getExtractMetadata();
  
  public void setExtractMetadata(Boolean extractMetadata);
  
  public Boolean getExtractRendition();
  
  public void setExtractRendition(Boolean extractRendition);
  
  public String getExtension();
  
  public void setExtension(String extension);
}
