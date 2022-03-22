package com.cs.dam.upload.assetinstance;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

import com.cs.constants.DAMConstants;
import com.cs.core.config.interactor.model.asset.IAssetDocumentKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetImageKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetVideoKeysModel;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.dam.AssetUtils;

/**
 * This class will contain methods for Asset Upload processing.
 * @author vannya.kalani
 *
 */
abstract class AbstractUpload implements IAssetUpload {
  
  public static final String  EXTENSION         = "extension";
  public static final String  ASSET_OBJECT_KEY  = "assetObjectKey";
  public static final String  IMAGE_KEY         = "imageKey";
  public static final String  THUMB_KEY         = "thumbKey";
  public static final String  PREVIEW_KEY       = "previewKey";
  public static final String  ASSET_DATA        = "assetData";
  public static final String  NAME              = "name";
  public static final String  MP4_KEY           = "mp4Key";
  
  /**
   * This method BufferedImage object to bytes array.
   * @param img
   * @param extension
   * @return the byte content of the image
   * @throws IOException 
   * @throws Exception
   */
  protected byte[] imageToBytes(BufferedImage img, String extension) throws IOException {
    byte[] imageInByte;
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      ImageIO.write(img, extension, baos);
      baos.flush();
      imageInByte = baos.toByteArray();
    }
    return imageInByte;
  }

  /**
   * This method creates the request model for asset upload.
   * @param fileModel
   * @param container
   * @param fileFormat
   * @param thumbImage
   * @param sourcePath
   * @param authToken
   * @param storagePath
   * @param metaType
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  protected Map<String, Object> fillUploadData(Map<String, Object> fileModel, String container,
      String fileFormat, String authToken, String storagePath, String metaType, String contentType)
  {
    Map<String, Object> uploadDataModel = AssetUtils.fillUploadCommonData(fileModel, container, fileFormat,
        authToken, storagePath, metaType, contentType);
    Map<String, Object> assetDataMap = (Map<String, Object>) uploadDataModel.get(ASSET_DATA);
    
    switch (metaType) {
      case DAMConstants.TYPE_THUMBNAIL:
        assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_ORIGINAL, fileModel.get(ASSET_OBJECT_KEY));
        uploadDataModel.put(ASSET_OBJECT_KEY, fileModel.get(THUMB_KEY));
        break;
      case DAMConstants.TYPE_PREVIEW:
        assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_ORIGINAL, fileModel.get(ASSET_OBJECT_KEY));
        uploadDataModel.put(ASSET_OBJECT_KEY, fileModel.get(PREVIEW_KEY));
        break;
      case DAMConstants.TYPE_ORIGINAL:
        assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_THUMB, fileModel.get(THUMB_KEY));
        assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_PREVIEW_ID, fileModel.get(PREVIEW_KEY));
        uploadDataModel.put(ASSET_OBJECT_KEY, fileModel.get(ASSET_OBJECT_KEY));
        break;
    }
    
    return uploadDataModel;
  }
  
  protected Map<String, Object> fillReturnModel(Map<String, Object> metadata, String hash,
      String name, String extension, Map<String, Object> fileModel, String klassID, String assetType)
  {
    Map<String, Object> returnModel = new HashMap<>();
    returnModel.put("fileName", name);
    returnModel.put(IAssetKeysModel.IMAGE_KEY, fileModel.get(ASSET_OBJECT_KEY));
    returnModel.put(ASSET_OBJECT_KEY, fileModel.get(ASSET_OBJECT_KEY));
    returnModel.put(IAssetImageKeysModel.THUMB_KEY, fileModel.get(THUMB_KEY));
    returnModel.put("klassId", klassID);
    returnModel.put("hash", hash);
    returnModel.put("metadata", metadata);
    returnModel.put("type", assetType);
    
    switch (assetType) {
      case "Video":
        returnModel.put(IAssetVideoKeysModel.MP4_KEY, fileModel.get("mp4Key"));
        break;
      case "Document":
        returnModel.put(IAssetDocumentKeysModel.PREVIEW_KEY, fileModel.get(PREVIEW_KEY));
        break;
    }
    
    return returnModel;
  }
  
  protected Map<String, Object> getBasicInformationOfFile(String fileSourcePath) throws RDBMSException
  {
    Map<String, Object> basicInfoMap = new HashMap<>();
    basicInfoMap.put(EXTENSION, FilenameUtils.getExtension(fileSourcePath));
    basicInfoMap.put(NAME, fileSourcePath);
    basicInfoMap.put(ASSET_OBJECT_KEY, RDBMSAppDriverManager.getDriver().newUniqueID( BaseType.ASSET.getPrefix()));
    basicInfoMap.put(THUMB_KEY, RDBMSAppDriverManager.getDriver().newUniqueID( BaseType.ASSET.getPrefix()));
    
    return basicInfoMap;
  }

}
