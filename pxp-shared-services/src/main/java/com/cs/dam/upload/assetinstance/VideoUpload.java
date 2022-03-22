package com.cs.dam.upload.assetinstance;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.cs.constants.DAMConstants;
import com.cs.constants.FileExtensionConstants;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.services.CSDAMServer;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.dam.asset.exceptions.FFMPEGException;
import com.cs.dam.asset.processing.VideoAssetConverter;
import com.cs.utils.ExceptionUtil;
import com.cs.utils.dam.AssetUtils;

public class VideoUpload extends AbstractUpload {
  
  private static VideoUpload instance = null;
  
  private VideoUpload() {
    // private constructor for Singleton class.
  }
  
  /**
   * Use this method to get VideoUpload instance
   * @return VideoUpload instance
   */
  public static VideoUpload getInstance() {
    if(instance == null) {
      instance = new VideoUpload();
    }
    return instance;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> executeUpload(String fileSourcePath, Map<String, Object> metadata,
      String hash, Boolean extractRendition, String storagePath, String authToken, String klassID,
      Boolean shouldExtractMetadata, String container, IExceptionModel warnings, 
      String thumbnailPath, boolean isInDesignServerEnabled) throws Exception
  {
    String fileName = FilenameUtils.getName(fileSourcePath);
    Map<String, Object> fileModel = getBasicInformationOfFile(fileSourcePath);
    String extension = (String) fileModel.get(EXTENSION);
    container = container != null ? container : DAMConstants.SWIFT_CONTAINER_VIDEO;
    Boolean isFileMp4 = extension.equalsIgnoreCase(FileExtensionConstants.MP4);
    
    String mp4Key = Boolean.TRUE.equals(isFileMp4) ? (String) fileModel.get(ASSET_OBJECT_KEY)
        : RDBMSAppDriverManager.getDriver().newUniqueID(BaseType.ASSET.getPrefix());
    fileModel.put(MP4_KEY, mp4Key);
    
    if (Boolean.TRUE.equals(extractRendition)) {
      try {
        /** Thumbnail **/
        uploadThumbnail(fileModel, fileSourcePath, storagePath, authToken, container);
        /** MP4 Preview **/
        uploadMp4(fileSourcePath, storagePath, authToken, isFileMp4, container, fileModel);
      }
      catch (FFMPEGException e) {
        ExceptionUtil.addFailureDetailsToFailureObject(warnings, e,
            null, fileName);
      }
    }
    
    /** Original **/
    Map<String, Object> originalVideoUploadModel = AssetUtils.fillUploadCommonData(fileModel, container,
        extension, authToken, storagePath, DAMConstants.TYPE_ORIGINAL, DAMConstants.CONTENT_TYPE_VIDEO);
    
    try (InputStream targetStream = new FileInputStream(new File(fileSourcePath))) {
      byte[] bytes = IOUtils.toByteArray(targetStream);
      originalVideoUploadModel.put("assetBytes", bytes);
    }
    
    ((Map<String, Object>) originalVideoUploadModel.get(ASSET_DATA))
        .put(DAMConstants.REQUEST_HEADER_OBJECT_META_THUMB, fileModel.get(THUMB_KEY));
    originalVideoUploadModel.put(DAMConstants.REQUEST_HEADER_OBJECT_META_MP4_ID,fileModel.get(MP4_KEY));
    originalVideoUploadModel.put(ASSET_OBJECT_KEY, fileModel.get(ASSET_OBJECT_KEY));
    
    CSDAMServer.instance().uploadAsset(originalVideoUploadModel);
    
    return fillReturnModel(metadata, hash, fileName + "." + extension, extension, fileModel,
        klassID, "Video");
  }
  
  private void uploadMp4(String fileSourcePath, String storagePath, String authToken,
      Boolean isFileMp4, String container, Map<String, Object> fileModel)
      throws IOException, FFMPEGException, CSInitializationException, PluginException
  {
    if (Boolean.FALSE.equals(isFileMp4)) {
      Map<String, Object> uploadDataModel = AssetUtils.fillUploadCommonData(fileModel, container, FileExtensionConstants.MP4,
          authToken, storagePath, DAMConstants.TYPE_MP4, DAMConstants.CONTENT_TYPE_VIDEO);
      byte[] mp4VideoBytes = VideoAssetConverter.convertVideo(fileSourcePath);
      uploadDataModel.put(ASSET_OBJECT_KEY, fileModel.get(MP4_KEY));
      uploadDataModel.put("assetBytes", mp4VideoBytes);
      
      CSDAMServer.instance().uploadAsset(uploadDataModel);
    }
  }
  
  private void uploadThumbnail(Map<String, Object> fileModel, String fileSourcePath,
      String storagePath, String authToken, String container) throws Exception
  {
    BufferedImage videoThumbnail = VideoAssetConverter.getVideoThumbnail(fileSourcePath);
    String thumbFileFormat = FileExtensionConstants.PNG;
    Map<String, Object> thumbUploadDataModel = fillUploadData(fileModel, container, thumbFileFormat,
        authToken, storagePath, DAMConstants.TYPE_THUMBNAIL, DAMConstants.CONTENT_TYPE_IMAGE);
    thumbUploadDataModel.put("assetBytes", imageToBytes(videoThumbnail, thumbFileFormat));
    
    CSDAMServer.instance().uploadAsset(thumbUploadDataModel);
  }
}
