package com.cs.dam.upload.assetinstance;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.DAMConstants;
import com.cs.constants.FileExtensionConstants;
import com.cs.core.config.interactor.model.asset.IAssetImageKeysModel;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.services.CSDAMServer;
import com.cs.dam.asset.exceptions.ImageMagickException;
import com.cs.dam.asset.processing.ImageConverter;
import com.cs.utils.ExceptionUtil;
import com.cs.utils.dam.AssetUtils;

public class ImageUpload extends AbstractUpload {
  
  private static ImageUpload instance = null;
  
  private ImageUpload() {
    // private constructor for Singleton class.
  }
  
  /**
   * Use this method to get ImageUpload instance
   * @return ImageUpload instance
   */
  public static ImageUpload getInstance() {
    if(instance == null) {
      instance = new ImageUpload();
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
    // For image upload container will be Image.
    String fileName = FilenameUtils.getName(fileSourcePath);
    Map<String, Object> fileModel = getBasicInformationOfFile(fileSourcePath);
    String extension = (String) fileModel.get(EXTENSION);
    String imageType = DAMConstants.TYPE_ORIGINAL;
    container = container != null ? container : DAMConstants.SWIFT_CONTAINER_IMAGE;
    Map<String, Integer> imageDimensions = new HashMap<>();
    
    if (Boolean.TRUE.equals(extractRendition)) {
      try {
        imageDimensions = ImageConverter.getImageDimensions(fileSourcePath);
        imageType = uploadThumbnail(fileModel, fileSourcePath, container, imageType, authToken,
            storagePath, imageDimensions, thumbnailPath);
      }
      catch (ImageMagickException e) {
        ExceptionUtil.addFailureDetailsToFailureObject(warnings, e, null, fileName);
      }
    }
    else {
      fileModel.put(THUMB_KEY, "");
    }

    /** * ORIGINAL ** */
    Map<String, Object> originalUploadDataModel = AssetUtils.fillUploadCommonData(fileModel, container,
        extension, authToken, storagePath, imageType, DAMConstants.CONTENT_TYPE_IMAGE);

    try (InputStream targetStream = new FileInputStream(new File(fileSourcePath))) {
      byte[] bytes = IOUtils.toByteArray(targetStream);
      originalUploadDataModel.put("assetBytes", bytes);
    }
    
    ((Map<String, Object>) originalUploadDataModel.get(ASSET_DATA))
        .put(DAMConstants.REQUEST_HEADER_OBJECT_META_THUMB, fileModel.get(THUMB_KEY));
    originalUploadDataModel.put(ASSET_OBJECT_KEY, fileModel.get(ASSET_OBJECT_KEY));
    
    CSDAMServer.instance().uploadAsset(originalUploadDataModel);
    
    return fillReturnModel(metadata, hash, fileName, extension, fileModel, imageDimensions,
        klassID, fileSourcePath);
  }
  
  private Map<String, Object> fillReturnModel(Map<String, Object> metadata, String hash,
      String name, String extension, Map<String, Object> fileModel,
      Map<String, Integer> imageDimensions, String klassID, String fileSourcePath)
  {
    Map<String, Object> returnModel = super.fillReturnModel(metadata, hash, name, extension,
        fileModel, klassID, DAMConstants.SWIFT_CONTAINER_IMAGE);
    returnModel.put(DAMConstants.WIDTH, imageDimensions.get(DAMConstants.WIDTH));
    returnModel.put(DAMConstants.HEIGHT, imageDimensions.get(DAMConstants.HEIGHT));
    returnModel.put(DAMConstants.THUMBNAIL_PATH, fileModel.get(DAMConstants.THUMBNAIL_PATH));
    returnModel.put(IAssetImageKeysModel.FILE_PATH, fileSourcePath);
    
    return returnModel;
  }
  
  private String uploadThumbnail(Map<String, Object> fileModel, String fileSourcePath,
      String container, String imageType, String authToken, String storagePath,
      Map<String, Integer> imageDimensions, String thumbnailPath) throws Exception
  {
    String extension = (String) fileModel.get(EXTENSION);
    
    if (isThumbRequired(imageDimensions, extension)) {
      String fileFormat = extension.equals(FileExtensionConstants.JPG)
          || extension.equals(FileExtensionConstants.JPEG) ? extension : FileExtensionConstants.PNG;
      
      BufferedImage thumbImage = null;
      if (thumbnailPath != null) {
        thumbImage = ImageIO.read(new File(thumbnailPath));
        fileFormat = FilenameUtils.getExtension(thumbnailPath);
      }
      else {
        String thumbnailDirectoryPath = AssetUtils.getFilePath() + RDBMSAppDriverManager.getDriver()
            .newUniqueID(IStandardConfig.UniquePrefix.RANDOM.getPrefix());
        String thumbFileName = DAMConstants.TEMP_CONVERSION_FILE_NAME_PREFIX + RDBMSAppDriverManager.getDriver()
                .newUniqueID(IStandardConfig.UniquePrefix.RANDOM.getPrefix()) + FileExtensionConstants.PNG_EXT;
        String thumbImagePath = thumbnailDirectoryPath + File.separator + thumbFileName;
        File directory = new File(thumbnailDirectoryPath);
        directory.mkdirs();
        thumbImage = ImageConverter.convertImage(fileSourcePath, thumbImagePath);
        fileModel.put(DAMConstants.THUMBNAIL_PATH, thumbImagePath);
      }
      
      Map<String, Object> thumbUploadDataModel = fillUploadData(fileModel, container, fileFormat,
          authToken, storagePath, DAMConstants.TYPE_THUMBNAIL, DAMConstants.CONTENT_TYPE_IMAGE);
      thumbUploadDataModel.put("assetBytes", imageToBytes(thumbImage, fileFormat));
      
      CSDAMServer.instance().uploadAsset(thumbUploadDataModel);
    }
    else {
      fileModel.put(THUMB_KEY, fileModel.get(ASSET_OBJECT_KEY));
      imageType = DAMConstants.TYPE_ORIGINAL_THUMBNAIL;
      fileModel.put(DAMConstants.THUMBNAIL_PATH, fileSourcePath);
    }
    return imageType;
  }
  
  /**
   * This method checks if a thumbnail of an image is required or not. If image
   * extension is among ai, eps, psd, ico it will always returns true. If the
   * size of the image is less than 200x200, it returns false, else it returns
   * true.
   * 
   * @param imageDimensions
   * @param extension : extension of image
   * @return boolean: whether thumbnail is required
   */
  protected boolean isThumbRequired(Map<String, Integer> imageDimensions, String extension)
  {
    if (FileExtensionConstants.AI.equals(extension) || FileExtensionConstants.EPS.equals(extension)
        || FileExtensionConstants.PSD.equals(extension) || FileExtensionConstants.ICO.equals(extension)) {
      return true;
    }
    return (imageDimensions.get(DAMConstants.HEIGHT) > 200 || imageDimensions.get(DAMConstants.WIDTH) > 200);
  }
}
