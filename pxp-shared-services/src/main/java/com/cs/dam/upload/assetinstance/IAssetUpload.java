package com.cs.dam.upload.assetinstance;

import java.util.Map;

import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;

public interface IAssetUpload {

  /**
   * Upload asset service
   *
   * @param fileSourcePath
   * @param metadata
   * @param hash
   * @param extractRendition
   * @param storagePath
   * @param authToken
   * @param klassID 
   * @param warnings 
   * @return asset generic map of information
   * @throws Exception
   */
  public Map<String, Object> executeUpload(String fileSourcePath, Map<String, Object> metadata,
          String hash, Boolean extractRendition, String storagePath, String authToken, String klassID,
          Boolean shouldExtractMetadata, String container, IExceptionModel warnings, 
          String thumbnailPath, boolean isInDesignServerEnabled) throws Exception;
}
