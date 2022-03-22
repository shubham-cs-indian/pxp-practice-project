package com.cs.utils.dam;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.simple.parser.ParseException;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.DAMConstants;
import com.cs.core.config.interactor.model.asset.AssetDocumentKeysModel;
import com.cs.core.config.interactor.model.asset.AssetFileModel;
import com.cs.core.config.interactor.model.asset.AssetImageKeysModel;
import com.cs.core.config.interactor.model.asset.AssetVideoKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetDocumentKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetExtensionConfiguration;
import com.cs.core.config.interactor.model.asset.IAssetExtensionConfigurationModel;
import com.cs.core.config.interactor.model.asset.IAssetFileModel;
import com.cs.core.config.interactor.model.asset.IAssetImageKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetVideoKeysModel;
import com.cs.core.config.interactor.model.asset.IGetAssetDetailsRequestModel;
import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.exception.assetserver.AssetFileTypeNotSupportedException;
import com.cs.core.runtime.interactor.exception.assetserver.ExtractedFileNotProcessedException;
import com.cs.core.runtime.interactor.exception.assetserver.ExtractedFileTypeNotSupportedException;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.services.CSDAMServer;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.asset.exceptions.ExifToolException;
import com.cs.dam.asset.processing.ExtractMetadata;
import com.cs.dam.asset.processing.ImageConverter;
import com.cs.dam.upload.assetinstance.DocumentUpload;
import com.cs.dam.upload.assetinstance.IAssetUpload;
import com.cs.dam.upload.assetinstance.ImageUpload;
import com.cs.dam.upload.assetinstance.VideoUpload;
import com.cs.utils.ExceptionUtil;

/**
 * This is for asset related utility methods.
 * 
 * @author pranav.huchche
 *
 */
public class AssetUtils {
  
  
  private AssetUtils()
  {
    // Private constructor
  }
  
  private static String assetFilesPath;
  
  static {
    try {
      assetFilesPath = CSProperties.instance()
          .getString("upload.asset.entities.filePath");
    }
    catch (CSInitializationException e) {
      System.out.println(e.toString());
    }
  }

  private static final String EXTRACT_METADATA        = "extractMetadata";
  private static final String KLASS_ID                = "klassId";
  private static final String EXTENSION_TYPE          = "extensionType";
  private static final String IS_EXTRACTED            = "isExtracted";
  private static final String PATH                    = "path";
  private static final String EXTENSION_CONFIGURATION = "extensionConfiguration";
  private static final String EXTRACT_RENDITION       = "extractRendition";
  private static final String IN_DESIGN_SERVER_ENABLED= "isInDesignServerEnabled";
  private static final String CONTAINER               = "container";
  
  private static final String STORAGE_URL             = "storageUrl";
  public static final String  ASSET_DATA              = "assetData";
 
  public static String getFilePath()
  {
    File dir = new File(assetFilesPath);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    return assetFilesPath;
  }
  
  public static void deleteFileAndDirectory(String filePath)
  {
    if (filePath == null) {
      return;
    }
    
    try {
      File directory = new File(filePath.substring(0, filePath.lastIndexOf(File.separator)));
      FileUtils.forceDelete(directory);
      System.out.println("******************************************************************");
      System.out.println("Directory successfully deleted!");
      System.out.println("Path: " + filePath);
      System.out.println("******************************************************************");
    }
    catch (IOException ie) {
      System.out.println("******************************************************************");
      System.out.println("Directory not deleted!");
      System.out.println("Path: " + filePath);
      System.out.println("******************************************************************");
      ie.printStackTrace();
      RDBMSLogger.instance().exception(ie);
      System.out.println("******************************************************************");
      System.out.println("******************************************************************");
    }
  }
  
  public static void deleteFileAndDirectory(File directory)
  {
    if (directory == null) {
      return;
    }
    try {
      FileUtils.forceDelete(directory);
      System.out.println("******************************************************************");
      System.out.println("Directory successfully deleted!");
      System.out.println("Path: " + directory);
      System.out.println("******************************************************************");
    }
    catch (IOException ie) {
      System.out.println("******************************************************************");
      System.out.println("Directory not deleted!");
      System.out.println("Path: " + directory);
      System.out.println("******************************************************************");
      ie.printStackTrace();
      RDBMSLogger.instance().exception(ie);
      System.out.println("******************************************************************");
      System.out.println("******************************************************************");
    }
  }
  
  public static byte[] getBytesFromFile(File file) throws IOException
  {
    InputStream targetStream = new FileInputStream(file);
    byte[] bytes = IOUtils.toByteArray(targetStream);
    targetStream.close();
    return bytes;
  }
  
  public static String hashFile(String sourcePath) throws Exception
  {
    try (FileInputStream inputStream = new FileInputStream(sourcePath)) {
      return generateHashCodeByInputStream(inputStream);
    }
    catch (NoSuchAlgorithmException | IOException ex) {
      throw new Exception("Could not generate hash from file", ex);
    }
  }
  
  public static String convertByteArrayToHexString(byte[] arrayBytes)
  {
    StringBuffer stringBuffer = new StringBuffer();
    for (int i = 0; i < arrayBytes.length; i++) {
      stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
          .substring(1));
    }
    return stringBuffer.toString();
  }
  
  public static void getExtensionConfigurationDetails(
      IAssetConfigurationDetailsResponseModel assetModel, String extension, String natureType,
      Map<String, Object> extensionConfigurationReturnMap)
  {
    Map<String, List<IAssetExtensionConfigurationModel>> allExtensionConfiguration = assetModel
        .getExtensionConfiguration();
    List<IAssetExtensionConfigurationModel> extensionConfigurationListFromNatureType = allExtensionConfiguration
        .get(natureType);
    if (extensionConfigurationListFromNatureType != null) {
      extensionConfigurationReturnMap.put(IAssetFileModel.EXTENSION_TYPE, natureType);
      extensionConfigurationReturnMap.put(IAssetFileModel.EXTENSION_CONFIGURATION,
          getExtensionConfigurationFromList(extensionConfigurationListFromNatureType, extension));
    }
    else {
      getExtensionConfiguration(extensionConfigurationReturnMap, allExtensionConfiguration,
          extension);
    }
  }
  
  public static void getExtensionConfiguration(Map<String, Object> extensionConfigurationReturnMap,
      Map<String, List<IAssetExtensionConfigurationModel>> allExtensionConfiguration,
      String extension)
  {
    allExtensionConfiguration.forEach((keyNatureType, valueExtensionConfigurationList) -> {
      IAssetExtensionConfigurationModel extensionConfiguration = getExtensionConfigurationFromList(
          allExtensionConfiguration.get(keyNatureType), extension);
      if (extensionConfiguration != null) {
        extensionConfigurationReturnMap.put(IAssetFileModel.EXTENSION_CONFIGURATION,
            extensionConfiguration);
        extensionConfigurationReturnMap.put(IAssetFileModel.EXTENSION_TYPE, keyNatureType);
      }
    });
  }
  
  public static IAssetExtensionConfigurationModel getExtensionConfigurationFromList(
      List<IAssetExtensionConfigurationModel> extensionConfigurationList, String extension)
  {
    for (IAssetExtensionConfigurationModel extensionConfiguration : extensionConfigurationList) {
      if (extensionConfiguration.getExtension()
          .equals(extension)) {
        return extensionConfiguration;
      }
    }
    return null;
  }
  
  public static boolean isWindows()
  {
    boolean isWindows = System.getProperty("os.name")
        .toLowerCase()
        .contains("win");
    return isWindows;
  }
  
  public static Map<String, Object> handleFile(Map<String, Object> fileModel) throws Exception
  {
    return handleFile(fileModel, new HashSet<String>());
  }
  
  public static IAssetKeysModel handleFile(IAssetFileModel fileModel) throws Exception
  {
    Map<String, Object> fileModelMap = new HashMap<>();
    String fileName = fileModel.getName() + "." + fileModel.getExtension();
    String extensionType = fileModel.getExtensionType();
    fileModelMap.put(PATH, fileModel.getPath());
    fileModelMap.put(IS_EXTRACTED, fileModel.getIsExtracted());
    fileModelMap.put(EXTENSION_TYPE, extensionType);
    fileModelMap.put(KLASS_ID, fileModel.getKlassId());
    fileModelMap.put(CONTAINER, fileModel.getContainer());
    IAssetExtensionConfiguration extensionConfiguration = fileModel.getExtensionConfiguration();
    Map<String, Object> extensionConfigurationMap = new HashMap<>();
    if(extensionConfiguration != null) {
      extensionConfigurationMap.put(EXTRACT_METADATA, extensionConfiguration.getExtractMetadata());
      extensionConfigurationMap.put(EXTRACT_RENDITION, extensionConfiguration.getExtractRendition());
    }
    fileModelMap.put(EXTENSION_CONFIGURATION, extensionConfigurationMap);
    fileModelMap.put(DAMConstants.THUMBNAIL_PATH, fileModel.getThumbnailPath());
    fileModelMap.put(DAMConstants.IS_INDESIGN_SERVER_ENABLED, fileModel.isInDesignServerEnabled());
    Map<String, Object> returnModel = handleFile(fileModelMap);
    
    return getAssetKeysModel(fileName, extensionType, returnModel);
  }

  @SuppressWarnings("unchecked")
  public static IAssetKeysModel getAssetKeysModel(String fileName, String extensionType, Map<String, Object> returnModel)
  {
    String imageKey = (String) returnModel.get(IAssetKeysModel.IMAGE_KEY);
    String thumbKey = (String) returnModel.get(IAssetKeysModel.THUMB_KEY);
    String hash = (String) returnModel.get(IAssetKeysModel.HASH);
    String key = (String) returnModel.get(IAssetKeysModel.KEY);
    String klassId = (String) returnModel.get(IAssetKeysModel.KLASS_ID);
    Map<String, Object> metadata = (Map<String, Object>) returnModel.get(IAssetKeysModel.METADATA);
    IExceptionModel warnings = (IExceptionModel) returnModel.get(IAssetKeysModel.WARNINGS);
    String thumbnailPath = (String) returnModel.get(DAMConstants.THUMBNAIL_PATH);
    String type = (String) returnModel.get(IAssetKeysModel.TYPE);
    
    IAssetKeysModel assetKeyModel;
    if (extensionType.equals(DAMConstants.MAM_NATURE_TYPE_IMAGE)) {
      Integer height = (Integer) returnModel.get(IAssetImageKeysModel.HEIGHT);
      Integer width = (Integer) returnModel.get(IAssetImageKeysModel.WIDTH);
      String filePath = (String) returnModel.get(IAssetImageKeysModel.FILE_PATH);
      assetKeyModel = new AssetImageKeysModel(imageKey, thumbKey, metadata, hash, height, width, key, fileName, klassId, thumbnailPath, filePath);
    }
    else if (extensionType.equals(DAMConstants.MAM_NATURE_TYPE_VIDEO)) {
      String mp4Key = (String) returnModel.get(IAssetVideoKeysModel.MP4_KEY);
      assetKeyModel = new AssetVideoKeysModel(imageKey, thumbKey, metadata, hash, mp4Key, key, fileName, klassId);
    }
    else if (extensionType.equals(DAMConstants.MAM_NATURE_TYPE_DOCUMENT)) {
      String previewKey = (String) returnModel.get(IAssetDocumentKeysModel.PREVIEW_KEY);
      assetKeyModel = new AssetDocumentKeysModel(imageKey, thumbKey, metadata, hash, previewKey, key, fileName, klassId);
    }
    else { // Unreachable code : as if these 3 conditions are not met, it will
           // throw error from handleFile(Map<String, Object> fileModel) method
      return null;
    }
    assetKeyModel.setWarnings(warnings);
    assetKeyModel.setType(type);
    return assetKeyModel;
  }
  
  @SuppressWarnings("unchecked")
  public static Map<String, Object> handleFile(Map<String, Object> fileModel,
      Set<String> idsForRedundancycheck) throws Exception
  {
    Map<String, Object> returnModel;
    IAssetUpload assetUpload;
    Map<String, Object> metadata = new HashMap<>();
    IExceptionModel warnings = new ExceptionModel();
    String fileSourcePath = (String) fileModel.get(PATH);
    String container = (String) fileModel.get(CONTAINER);
    String name = FilenameUtils.getName(fileSourcePath);
    boolean isExtracted = (boolean) fileModel.get(IS_EXTRACTED);
    String fileExtentionType = (String) fileModel.get(EXTENSION_TYPE);
    Map<String, Object> extensionConfiguration = (Map<String, Object>) fileModel.get(EXTENSION_CONFIGURATION);
    boolean shouldExtractMetaData = false;
    boolean shouldExtractRenditions = false;
    boolean isInDesignServerEnabled = fileModel.get(IN_DESIGN_SERVER_ENABLED) != null
        ? (boolean) fileModel.get(IN_DESIGN_SERVER_ENABLED) : false;
    if(extensionConfiguration != null && !extensionConfiguration.isEmpty()) {
      shouldExtractMetaData = (boolean) extensionConfiguration.get(EXTRACT_METADATA);
      shouldExtractRenditions = (boolean) extensionConfiguration.get(EXTRACT_RENDITION);
    }
    String thumbnailPath = (String) fileModel.get(DAMConstants.THUMBNAIL_PATH);
    
    try {
      if (extensionConfiguration == null || extensionConfiguration.isEmpty()) {
        if (isExtracted) {
          throw new ExtractedFileTypeNotSupportedException();
        }
        throw new AssetFileTypeNotSupportedException();
      }
      
      if (shouldExtractMetaData) {
        metadata = getMetadata(warnings, fileSourcePath, name, isExtracted);
      }
      metadata.put("name", name);
      
      String hash = hashFile(fileSourcePath);
      
      IJSONContent authenticateSwiftServer;
      authenticateSwiftServer = CSDAMServer.instance().getServerInformation();
      String storageUrl = authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_STORAGE_URL, "");
      String authTokenURL = authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_AUTH_TOKEN, "");
      
      if (fileExtentionType.equals(DAMConstants.MAM_NATURE_TYPE_IMAGE)) {
        assetUpload = ImageUpload.getInstance();
      }
      else if (fileExtentionType.equals(DAMConstants.MAM_NATURE_TYPE_VIDEO)) {
        assetUpload = VideoUpload.getInstance();
      }
      else if (fileExtentionType.equals(DAMConstants.MAM_NATURE_TYPE_DOCUMENT)) {
        assetUpload = DocumentUpload.getInstance();
      }
      else {
        throw new AssetFileTypeNotSupportedException();
      }
      String klassId = (String) fileModel.get(KLASS_ID);
      returnModel = assetUpload.executeUpload(fileSourcePath, metadata, hash, shouldExtractRenditions,
          storageUrl, authTokenURL, klassId, shouldExtractMetaData, container, warnings, thumbnailPath, isInDesignServerEnabled);
      returnModel.put(KLASS_ID, klassId);
      returnModel.put("warnings", warnings);
      
      return returnModel;
    }
    catch (Exception e) {
      System.out.println("Exception in handlefile ::"+e);
      throw e;
    }
  }
  
  
  /** Upload icon asset file to the swift server
   * @param fileModel
   * @return fileName
   * @throws Exception
   */
  /*public static  Map<String, Object> handleIconUpload(Map<String, Object> fileModel) throws Exception
  {
    String fileSourcePath = (String) fileModel.get(DAMConstants.FILE_MODEL_PATH);
    String container = (String) fileModel.get(DAMConstants.FILE_MODEL_CONTAINER);
    try {
      IJSONContent authenticateSwiftServer = CSDAMServer.instance().getServerInformation();
      String storageUrl = authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_STORAGE_URL, "");
      String authTokenURL = authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_AUTH_TOKEN, "");
      
      String fileName = FilenameUtils.getName(fileSourcePath);
      String extension = FilenameUtils.getExtension(fileSourcePath);
      String imageType = DAMConstants.TYPE_ORIGINAL;
      container = container != null ? container : DAMConstants.SWIFT_CONTAINER_ICONS;
      
      fileModel.put(IAssetFileModel.NAME, fileName);
      Map<String, Object> originalUploadDataModel = fillUploadCommonData(fileModel, container, extension, authTokenURL, storageUrl,
          imageType, DAMConstants.CONTENT_TYPE_IMAGE);
      
      try (InputStream targetStream = new FileInputStream(new File(fileSourcePath))) {
        byte[] bytes = IOUtils.toByteArray(targetStream);
        originalUploadDataModel.put(IAssetUploadDataModel.ASSET_BYTES, bytes);
      }
      
      originalUploadDataModel.put(DAMConstants.ASSET_OBJECT_KEY, fileModel.get(DAMConstants.ASSET_OBJECT_KEY));
  
      CSDAMServer.instance().uploadAsset(originalUploadDataModel);
      
      Map<String, Object> returnModel = new HashMap<String, Object>();
      returnModel.put(DAMConstants.FILE_NAME, fileName);
      return returnModel;
    }
    catch (Exception e) {
      throw e;
    }
  }*/
  
  
  /** Fill Common Data in asset upload model
   * @param fileModel
   * @param container
   * @param fileFormat
   * @param authToken
   * @param storagePath
   * @param metaType
   * @param contentType
   * @return asset upload model
   */
  public static Map<String, Object> fillUploadCommonData(Map<String, Object> fileModel,
      String container, String fileFormat, String authToken, String storagePath,
      String metaType, String contentType)
  {
    Map<String, Object> assetDataMap = new HashMap<>();
    Map<String, Object> uploadDataModel = new HashMap<>();
    assetDataMap.put(DAMConstants.REQUEST_HEADER_AUTH_TOKEN, authToken);
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_NAME, fileModel.get("name"));
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_FORMAT, fileFormat);
    String thumbContentType = contentType + fileFormat;
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_CONTENT_TYPE, thumbContentType);
    assetDataMap.put(DAMConstants.REQUEST_HEADER_CONTENT_TYPE, thumbContentType);
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_TYPE, metaType);
    assetDataMap.put(DAMConstants.REQUEST_HEADER_OBJECT_META_ORIGINAL, fileModel.get(DAMConstants.ASSET_OBJECT_KEY));
    
    uploadDataModel.put(ASSET_DATA, assetDataMap);
    uploadDataModel.put(STORAGE_URL, storagePath);
    uploadDataModel.put(DAMConstants.FILE_MODEL_CONTAINER, container);
    
    return uploadDataModel;
  }
  
  private static Map<String, Object> getMetadata(IExceptionModel warnings,
      String fileSourcePath, String name, boolean isExtracted)
      throws ParseException
  {
    Map<String, Object> metadata = new HashMap<>();
    try {
      metadata = ExtractMetadata.extractMetadataFromFile(fileSourcePath);
    }
    catch (ExifToolException exifToolException) {
      if (isExtracted) {
        ExceptionUtil.addFailureDetailsToFailureObject(warnings,
            new ExtractedFileNotProcessedException(), null,
            name);
      }
      else {
        ExceptionUtil.addFailureDetailsToFailureObject(warnings, exifToolException, null,
            name);
      }
    }
    return metadata;
  }
  
  /**
   * If any changes are made in below function (reAuthenticateAndUpload), make
   * changes in similar function in AssetUploadTask & VideoConvertAndUploadTask
   * as well
   */
  /* public static void reAuthenticateAndUpload(IAssetUploadDataModel uploadDataModel,
      Map<String, String> thumbDataMap) throws Exception
  {
    IAssetServerDetailsModel assetServerDetailsFromStrategy = authenticateAssetServerStrategy
        .execute(null);
    assetServerDetails = assetServerDetailsFromStrategy;
    uploadDataModel.setStorageUrl(assetServerDetails.getStorageURL());
    thumbDataMap.put(REQUEST_HEADER_AUTH_TOKEN, assetServerDetails.getAuthToken());
    uploadDataModel.setAssetDataMap(thumbDataMap);
    uploadAssetToServerStrategy.execute(uploadDataModel);
  }*/
  
  public static byte[] generateBytes(BufferedImage img, String extension) throws Exception
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(img, extension, baos);
    baos.flush();
    byte[] imageInByte = baos.toByteArray();
    baos.close();
    return imageInByte;
  }
  
  public static IAssetImageKeysModel getResponseForImageUpload(String sourcePath, String imageKey,
      String thumbKey, String extension, String hash, Map<String, Object> metadata,
      Map<String, Integer> imageDimensions, String key, String name, String klassId)
      throws Exception
  {
    Integer width = imageDimensions.get(DAMConstants.WIDTH);
    Integer height = imageDimensions.get(DAMConstants.HEIGHT);
    String fileName = name + "." + extension;
    return new AssetImageKeysModel(imageKey, thumbKey, metadata, hash, height, width, key, fileName,
        klassId, null, sourcePath);
  }
  
  /**
   * This method checks if a thumbnail of an image is required or not. If the
   * size of the image is less than 200x200, it returns false, else it return
   * true;
   *
   * @param sourcePath:
   *          String - source path of the video file
   * @return boolean: whether thumbnail is required
   * @throws Exception
   */
  public static boolean isThumbRequired(String sourcePath, String extension) throws Exception
  {
    if (extension.equals("ai") || extension.equals("eps") || extension.equals("psd")
        || extension.equals("ico")) {
      return true;
    }
    Map<String, Integer> imageDimensions = ImageConverter.getImageDimensions(sourcePath);
    if (imageDimensions.get(DAMConstants.HEIGHT) <= 200 && imageDimensions.get(DAMConstants.WIDTH) <= 200) {
      return false;
    }
    return true;
  }
  
  public static IAssetFileModel getFileModelForDataInitialization(IMultiPartFileInfoModel multiPartFileInfoModel,
      String mode, List<IAssetFileModel> fileModelList,
      IAssetConfigurationDetailsResponseModel assetModel) throws UnsupportedEncodingException {
    
    Boolean shouldCheckForRedundancy = false;
    IAssetFileModel fileModel = null;
    
    Map<String, Object> extensionConfigurationReturnMap = new HashMap<>();
    
    String originalFilename = null;
    boolean isWindows = isWindows();
    if (isWindows) {
      originalFilename = new String(multiPartFileInfoModel.getOriginalFilename()
          .getBytes(), "Windows-1252");
    }
    else {
      originalFilename = multiPartFileInfoModel.getOriginalFilename();
    }
    
    int lastIndexOfDot = originalFilename.lastIndexOf(".");
    String fileName = originalFilename.substring(0, lastIndexOfDot);
    String extension = originalFilename.substring(lastIndexOfDot + 1)
        .toLowerCase();
    
    String sourcePath = multiPartFileInfoModel.getAdditionalProperty("path")+"";
    
    String container = null;
    String natureType = assetModel.getNatureType();
    
    getExtensionConfigurationDetails(assetModel, extension, natureType,
        extensionConfigurationReturnMap);
    
    IAssetExtensionConfigurationModel extensionConfiguration = (IAssetExtensionConfigurationModel) extensionConfigurationReturnMap
        .get(IAssetFileModel.EXTENSION_CONFIGURATION);
    
    switch (natureType) {
      case DAMConstants.MAM_NATURE_TYPE_IMAGE:
        container = DAMConstants.SWIFT_CONTAINER_IMAGE;
        break;
      case DAMConstants.MAM_NATURE_TYPE_VIDEO:
        container = DAMConstants.SWIFT_CONTAINER_VIDEO;
        break;
      case DAMConstants.MAM_NATURE_TYPE_DOCUMENT:
        container = DAMConstants.SWIFT_CONTAINER_DOCUMENT;
        break;
      default:
        if (mode != null) {
          if (mode.equalsIgnoreCase(DAMConstants.MODE_CONFIG)) {
            container = DAMConstants.SWIFT_CONTAINER_ICONS;
            shouldCheckForRedundancy = false;
          }
          else if (mode.equalsIgnoreCase(DAMConstants.MODE_ATTACHMENT)) {
            container = DAMConstants.SWIFT_CONTAINER_ATTACHMENT;
            shouldCheckForRedundancy = false;
          }
        }
    }
    
    fileModel = new AssetFileModel(container, multiPartFileInfoModel.getBytes(), fileName,
        extension, sourcePath, multiPartFileInfoModel.getKey(), shouldCheckForRedundancy, null,
        extensionConfiguration,
        (String) extensionConfigurationReturnMap.get(IAssetFileModel.EXTENSION_TYPE),
        assetModel.getKlassId(), null);
    
        return fileModel;
    
  }
  
  public static IAssetFileModel getFileModel(IMultiPartFileInfoModel multiPartFileInfoModel,
      String mode, List<IAssetFileModel> fileModelList,
      IAssetConfigurationDetailsResponseModel assetModel) throws Exception
  {
    Boolean shouldCheckForRedundancy = false;
    Boolean extractZip = false;
    boolean isInDesignServerEnabled = false;
    Map<String, Object> extensionConfigurationReturnMap = new HashMap<>();
    
    String sourcePath = null;
    String natureType = null;
    IAssetFileModel fileModel = null;
    Boolean exceptionOccured = false;
    
    try {
      String originalFilename = null;
      boolean isWindows = isWindows();
      if (isWindows) {
        originalFilename = new String(multiPartFileInfoModel.getOriginalFilename()
            .getBytes(), "Windows-1252");
      }
      else {
        originalFilename = multiPartFileInfoModel.getOriginalFilename();
      }
      int lastIndexOfDot = originalFilename.lastIndexOf(".");
      String fileName = originalFilename.substring(0, lastIndexOfDot);
      String extension = originalFilename.substring(lastIndexOfDot + 1)
          .toLowerCase();
      
      if (assetModel != null) {
        shouldCheckForRedundancy = assetModel.getDetectDuplicate();
        extractZip = assetModel.getUploadZip();
        isInDesignServerEnabled = assetModel.getIsIndesignServerEnabled();
        natureType = assetModel.getNatureType();
        getExtensionConfigurationDetails(assetModel, extension, natureType,
            extensionConfigurationReturnMap);
      }
      
      IAssetExtensionConfigurationModel extensionConfiguration = (IAssetExtensionConfigurationModel) extensionConfigurationReturnMap
          .get(IAssetFileModel.EXTENSION_CONFIGURATION);
      if (!(assetModel != null && extensionConfiguration == null)) {
        if (extension.equals(DAMConstants.ZIP) && extractZip) {
          File zipFile = convert(originalFilename, multiPartFileInfoModel.getBytes());
          Map<String, File> fileMap = unZipIt(zipFile);
          for (Map.Entry<String, File> entry : fileMap.entrySet()) {
            IAssetFileModel assetFile = getFileModel(entry.getKey(), entry.getValue(), mode,
                shouldCheckForRedundancy, assetModel.getExtensionConfiguration());
            assetFile.setIsExtracted(true);
            assetFile.setInDesignServerEnabled(isInDesignServerEnabled);
            fileModelList.add(assetFile);
          }
           return null;
        }
        else {
          File file = writeAssetFileOnServer(originalFilename);
          sourcePath = file.getPath();
          FileOutputStream fos = new FileOutputStream(file);
          fos.write(multiPartFileInfoModel.getBytes());
          fos.close();
        }
      }
      
      String container = null;
      
      switch (natureType) {
        case DAMConstants.MAM_NATURE_TYPE_IMAGE:
          container = DAMConstants.SWIFT_CONTAINER_IMAGE;
          break;
        case DAMConstants.MAM_NATURE_TYPE_VIDEO:
          container = DAMConstants.SWIFT_CONTAINER_VIDEO;
          break;
        case DAMConstants.MAM_NATURE_TYPE_DOCUMENT:
          container = DAMConstants.SWIFT_CONTAINER_DOCUMENT;
          break;
        default:
          if (mode != null) {
            if (mode.equalsIgnoreCase(DAMConstants.MODE_CONFIG)) {
              container = DAMConstants.SWIFT_CONTAINER_ICONS;
              shouldCheckForRedundancy = false;
            }
            else if (mode.equalsIgnoreCase(DAMConstants.MODE_ATTACHMENT)) {
              container = DAMConstants.SWIFT_CONTAINER_ATTACHMENT;
              shouldCheckForRedundancy = false;
            }
          }
      }
      
      fileModel = new AssetFileModel(container, multiPartFileInfoModel.getBytes(), fileName,
          extension, sourcePath, multiPartFileInfoModel.getKey(), shouldCheckForRedundancy, null,
          extensionConfiguration,
          (String) extensionConfigurationReturnMap.get(IAssetFileModel.EXTENSION_TYPE),
          assetModel.getKlassId(), null);
      fileModel.setInDesignServerEnabled(isInDesignServerEnabled);
      
    }
    catch (Exception e) {
      exceptionOccured = true;
      throw e;
    }
    finally {
      if (exceptionOccured) {
        deleteFileAndDirectory(sourcePath);
      }
    }
    return fileModel;
  }
  
  public static File writeAssetFileOnServer(String originalFilename) throws RDBMSException
  {
    String directoryPath = null;
    
    if (!assetFilesPath.isEmpty()) {
      directoryPath = assetFilesPath + "/" + RDBMSAppDriverManager.getDriver()
          .newUniqueID(IStandardConfig.UniquePrefix.RANDOM.getPrefix());
    }
    else {
      directoryPath = RDBMSAppDriverManager.getDriver()
          .newUniqueID(IStandardConfig.UniquePrefix.RANDOM.getPrefix());
    }
    
    File directory = new File(directoryPath);
    directory.mkdirs();
    File file = new File(directory, originalFilename);
    
    return file;
  }
  
  public static File convert(String originalFileName, byte[] fileData) throws Exception
  {
    File convFile = new File(originalFileName);
    convFile.createNewFile();
    try (FileOutputStream fos = new FileOutputStream(convFile);) {
      fos.write(fileData);
      return convFile;
    }
    catch (IOException ex) {
      throw new Exception("Could not generate hash from file", ex);
    }
  }
  
  public static IAssetFileModel getFileModel(String originalName, File file, String mode,
      Boolean shouldCheckForRedundancy,
      Map<String, List<IAssetExtensionConfigurationModel>> allExtensionConfiguration)
      throws Exception
  {
    IAssetFileModel fileModel = null;
    int pos = originalName.lastIndexOf(".");
    String fileName = originalName.substring(0, pos);
    String extension = originalName.substring(pos + 1)
        .toLowerCase();
    
    Map<String, Object> extensionConfigurationReturnMap = new HashMap<>();
    getExtensionConfiguration(extensionConfigurationReturnMap, allExtensionConfiguration,
        extension);
    String natureType = (String) extensionConfigurationReturnMap
        .get(IAssetFileModel.EXTENSION_TYPE);
    String klassId = getKlassIdAccordingToNatureType(natureType);
    
    InputStream targetStream = new FileInputStream(file);
    byte[] bytes = IOUtils.toByteArray(targetStream);
    targetStream.close();
    String container = null;
    String sourcePath = file.getAbsolutePath();
    if (mode != null) {
      if (mode.equalsIgnoreCase(DAMConstants.MODE_CONFIG)) {
        container = DAMConstants.SWIFT_CONTAINER_ICONS;
        shouldCheckForRedundancy = false;
      }
      else if (mode.equalsIgnoreCase(DAMConstants.MODE_ATTACHMENT)) {
        container = DAMConstants.SWIFT_CONTAINER_ATTACHMENT;
        // TODO : Read check for redandancy from property file
        shouldCheckForRedundancy = false;
      }
    }
    fileModel = new AssetFileModel(container, bytes, fileName, extension, sourcePath, null,
        shouldCheckForRedundancy, null,
        (IAssetExtensionConfigurationModel) extensionConfigurationReturnMap
            .get(IAssetFileModel.EXTENSION_CONFIGURATION),
        natureType, klassId, null);
    return fileModel;
  }
  
  public static String getKlassIdAccordingToNatureType(String natureType)
  {
    if (natureType == null) {
      return null;
    }
    switch (natureType) {
      case DAMConstants.MAM_NATURE_TYPE_IMAGE:
        return DAMConstants.IMAGE_KLASS;
      case DAMConstants.MAM_NATURE_TYPE_VIDEO:
        return DAMConstants.VIDEO_KLASS;
      case DAMConstants.MAM_NATURE_TYPE_DOCUMENT:
        return DAMConstants.DOCUMENT_KLASS;
      default:
        return null;
    }
  }
  
  /**
   * Unzip it
   *
   * @param zipFile
   *          input zip file
   * @return fileList that are to be uploaded in asset
   * @throws IOException
   * @throws RDBMSException 
   */
  public static Map<String, File> unZipIt(File zipFile) throws IOException, RDBMSException
  {
    byte[] buffer = new byte[1024];
    Map<String, File> fileMap = new HashMap<>();
    
    // create output directory is not exists
    // get the zip file content
    ZipInputStream zis = null;
    boolean isWindows = isWindows();
    if (isWindows) {
      zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), 1024),
          Charset.forName("Cp437"));
    }
    else {
      zis = new ZipInputStream(new FileInputStream(zipFile));
    }
    try {
      // get the zipped file list entry
      ZipEntry ze = zis.getNextEntry();
      
      while (ze != null) {
        if (ze.isDirectory()) {
          ze = zis.getNextEntry();
          continue;
        }
        String originalFileName = ze.getName();
        int pos2 = originalFileName.lastIndexOf("/");
        originalFileName = originalFileName.substring(pos2 + 1);
        File newFile = writeAssetFileOnServer(originalFileName);
        try (FileOutputStream fos = new FileOutputStream(newFile)) {
          int len;
          while ((len = zis.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
          }
          fileMap.put(originalFileName, newFile);
          
          ze = zis.getNextEntry();
        }
        catch (Exception e) {
          throw e;
        }
      }
    }
    finally {
      if (zis != null) {
        zis.closeEntry();
        zis.close();
      }
      zipFile.delete();
    }
    
    return fileMap;
  }
  
  /*private void uploadAsset(String fileSourcePath, IAssetUploadDataModel assetUploadDataModel)
      throws Exception
  {
    AssetUploadModel assetUploadModel = new AssetUploadModel();
    assetUploadModel.setAssetSourcePath(fileSourcePath);
    assetUploadModel.setAssetUploadDataModel(assetUploadDataModel);
    assetUpload.execute(assetUploadModel);
  }*/
  
  /**
   * This method returns the iid of instance for which isDuplicate column needs to be updated.
   * 1 is returned when instance have multiple duplicates.
   * 0 is returned when instance have no duplicates.
   * @param hash
   * @param baseEntityDAO
   * @param transactionDataMap
   * @return
   * @throws RDBMSException
   */
  public static long handleDuplicate(String hash, IBaseEntityDAO baseEntityDAO,
      Map<String, Object> transactionDataMap, long idToExclude) throws RDBMSException
  {
    List<Long> duplicateAssetIIds = baseEntityDAO.checkIfDuplicateAssetExist(hash, idToExclude,
        (String) transactionDataMap.get(ITransactionData.PHYSICAL_CATALOG_ID),
        (String) transactionDataMap.get(ITransactionData.ORGANIZATION_ID),
        (String) transactionDataMap.get(ITransactionData.ENDPOINT_ID));
    if (!duplicateAssetIIds.isEmpty()) {
      if (duplicateAssetIIds.size() == 1)
        return duplicateAssetIIds.get(0);
      else
        return 1;
    }
    else {
      return 0;
    }
  }
  
  /**
   * This method will update the duplicate status of entities. Value '1' for
   * duplicate id represent that only main instance needs to be updated
   * 
   * @param duplicateId
   * @param baseEntityDAO
   * @param localeCatlogDAO 
   * @throws RDBMSException
   */
  public static void updateDuplicateStatus(long duplicateId, IBaseEntityDAO baseEntityDAO, 
      ILocaleCatalogDAO localeCatlogDAO) throws RDBMSException
  {
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    long baseEntityIID = baseEntityDTO.getBaseEntityIID();
    
    if (duplicateId != 0) {
      Set<Long> baseEntityIIds = new HashSet<>();
      baseEntityIIds.add(baseEntityIID);
      if(duplicateId != 1) baseEntityIIds.add(duplicateId);
      baseEntityDAO.markAssetsDuplicateByIIds(baseEntityIIds, true);
    }
    else {
      Set<Long> baseEntityIIds = new HashSet<>();
      baseEntityIIds.add(baseEntityIID);
      baseEntityDAO.markAssetsDuplicateByIIds(baseEntityIIds, false);
    }
    
    if (localeCatlogDAO != null) {
      localeCatlogDAO.postUsecaseUpdate(baseEntityIID, IEventDTO.EventType.ELASTIC_UPDATE);
    }
  }
  
  /**
   * This method will update the duplicate status of entities to false which are
   * not duplicate anymore.
   * 
   * @param baseEntityDAO
   * @param hashCode
   * @param transactionDataMap
   * @param idToExclude
   * @param localeCatalogDAO 
   * @throws RDBMSException
   */
  public static void updateDuplicateStatusForReplacedAsset(IBaseEntityDAO baseEntityDAO,
      String hashCode, Map<String, Object> transactionDataMap, long idToExclude, ILocaleCatalogDAO localeCatalogDAO) throws RDBMSException
  {
    List<Long> duplicateIIds = baseEntityDAO.checkIfDuplicateAssetExist(hashCode, idToExclude,
        (String) transactionDataMap.get(ITransactionData.PHYSICAL_CATALOG_ID),
        (String) transactionDataMap.get(ITransactionData.ORGANIZATION_ID),
        (String) transactionDataMap.get(ITransactionData.ENDPOINT_ID));
    if (hashCode != null && duplicateIIds.size() == 1) {
      baseEntityDAO.markAssetsDuplicateByIIds(new HashSet<Long>(duplicateIIds), false);
      for (Long duplicateIId : duplicateIIds) {
        localeCatalogDAO.postUsecaseUpdate(duplicateIId, IEventDTO.EventType.ELASTIC_UPDATE);
      }
    }
  }
  
  /**
   * This method fetches asset from swift server along with its response code.
   * @param assetInfo
   * @return Map<String, Object>
   * @throws Exception
   */
  public static Map<String, Object> getAssetFromSwift(Map<String, String> assetInfo) throws Exception {
    Map<String,Object> requestMap = new HashMap<String,Object>();
    Map<String, Object> assetServerDetailsMap = new HashMap<>();
    IJSONContent authenticateSwiftServer = CSDAMServer.instance().getServerInformation();
    assetServerDetailsMap.put("storageUrl", authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_STORAGE_URL, ""));
    assetServerDetailsMap.put("authToken", authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_AUTH_TOKEN, ""));
    requestMap.put(IGetAssetDetailsRequestModel.ASSET_KEY, assetInfo.get("assetObjectKey"));
    requestMap.put(IGetAssetDetailsRequestModel.ASSET_SERVER_DETAILS, assetServerDetailsMap);
    requestMap.put(IGetAssetDetailsRequestModel.CONTAINER, assetInfo.get("type"));
    requestMap.put(IGetAssetDetailsRequestModel.REQUEST_HEADERS, new HashMap<>());
    
    return CSDAMServer.instance().getAsset(requestMap);
  }

  /**
   * Generate hash code using input stream
   * @param inputStream
   * @return
   * @throws NoSuchAlgorithmException
   * @throws IOException
   */
  public static String generateHashCodeByInputStream(InputStream inputStream) throws NoSuchAlgorithmException, IOException
  {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    
    byte[] bytesBuffer = new byte[1024];
    int bytesRead = -1;
    
    while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
      digest.update(bytesBuffer, 0, bytesRead);
    }
    
    byte[] hashedBytes = digest.digest();
    
    return convertByteArrayToHexString(hashedBytes);
  }
}
