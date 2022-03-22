package com.cs.core.config.interactor.usecase.assetserver;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cs.constants.DAMConstants;
import com.cs.constants.FileExtensionConstants;
import com.cs.core.asset.services.CommonConstants;
import com.cs.core.asset.services.MetadataUtils;
import com.cs.core.config.interactor.model.asset.IAssetDocumentKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetImageKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetKeysModel;
import com.cs.core.config.interactor.model.asset.IAssetVideoKeysModel;
import com.cs.core.config.interactor.usecase.tag.IGetAllAssetExtensions;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.runtime.interactor.entity.propertyinstance.IImageAttributeInstance;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSDAMServer;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.asset.exceptions.FFMPEGException;
import com.cs.dam.asset.exceptions.ImageMagickException;
import com.cs.dam.asset.processing.DocumentConverter;
import com.cs.dam.asset.processing.ExtractMetadata;
import com.cs.dam.asset.processing.ImageConverter;
import com.cs.dam.asset.processing.VideoAssetConverter;
import com.cs.di.workflow.tasks.ITransformationTask;
import com.cs.utils.dam.AssetUtils;

@Service
@SuppressWarnings("unchecked")
public class UploadAssetDataToServer {
  
  @Autowired
  IGetAllAssetExtensions            getAllAssetExtensions;
  
  @Autowired
  ThreadPoolTaskExecutor            migrationTaskExecutor;
  
  private static final String       EXTENSION                     = "extension";
  private static final String       ASSET_OBJECT_KEY              = "assetObjectKey";
  private static final String       THUMB_KEY                     = "thumbKey";
  private static final String       PREVIEW_KEY                   = "previewKey";
  private static final String       ASSET_DATA                    = "assetData";
  private static final String       NAME                          = "name";
  
  private static final String       IMAGE_ASSET                   = "image_asset";
  private static final String       VIDEO_ASSET                   = "video_asset";
  private static final String       DOCUMENT_ASSET                = "document_asset";
  
  private static final String       WIDTH                         = "width";
  private static final String       HEIGHT                        = "height";
  private static final String       STATUS                        = "status";
  private static final String       FAILURE_LOGS                  = "failureLogs";
  private static final String       SUCCESS_LOGS                  = "successLogs";
  
  public static final String        MP4_KEY                       = "mp4Key";
  
  private static final List<String> METADATA_PROPERTIES_TO_UPDATE = Arrays.asList("I_META_FILE_NAME", "I_MODIFICATION_DATE",
      "I_META_FILE_SIZE", "I_DIMENSIONS", "I_RESOLUTION", "assetdownloadcountattribute");
  
  public IVoidModel execute(Integer batchSize) throws Exception
  {
    if (batchSize == null || batchSize > 1000 || batchSize < 0) {
      batchSize = 500;
    }
    try {
      Map<String, List<String>> assetExtensions = getAllAssetExtensions.execute(new IdsListParameterModel()).getAssetExtensions();
      
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      InputStream stream = loader.getResourceAsStream("metadataPropertyMapping.json");
      JSONParser jsonParser = new JSONParser();
      Map<String, Object> metadataPropertyMapping = (JSONObject) jsonParser.parse(new InputStreamReader(stream, "UTF-8"));
      
      Integer totalRecords = getTotalRecordsCount();
      
      if (totalRecords == 0) {
        log("No records to process.", FAILURE_LOGS, null);
        return null;
      }
      
      int batches = totalRecords / batchSize;
      for (int batch = 0; batch <= batches; batch++) {
        Runnable workerThread = new UploadAssetsThread(assetExtensions, metadataPropertyMapping, batchSize, (batchSize * batch), batchSize);
        migrationTaskExecutor.execute(workerThread);
      }
    }
    catch (Exception e) {
      log("Can't intitate asset upload.", FAILURE_LOGS, e);
    }
    
    return null;
  }
  
  /**
   * @return
   * @throws SQLException
   */
  private Integer getTotalRecordsCount() throws SQLException
  {
    RDBMSAbstractDriver driver = (RDBMSAbstractDriver) RDBMSDriverManager.getDriver();
    Connection connection = DataSourceUtils.getConnection(driver.getDataSource());
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery("select count(*) from staging.\"assets\";");
    Integer totalRecords = 0;
    while (resultSet.next()) {
      totalRecords = resultSet.getInt(1);
    }
    
    resultSet.close();
    statement.close();
    connection.close();
    
    return totalRecords;
  }
  
  class UploadAssetsThread implements Runnable {
    
    private Map<String, List<String>> assetExtensions;
    private Map<String, Object>       metadataPropertyMapping;
    private Integer                   limit;
    private Integer                   offset;
    private Integer                   batchSize;
    
    public UploadAssetsThread(Map<String, List<String>> assetExtensions, Map<String, Object> metadataPropertyMapping, Integer limit,
        Integer offset, Integer batchSize)
    {
      this.assetExtensions = assetExtensions;
      this.metadataPropertyMapping = metadataPropertyMapping;
      this.limit = limit;
      this.offset = offset;
      this.batchSize = batchSize;
    }
    
    public void run()
    {
      RDBMSAbstractDriver driver = null;
      Connection connection = null;
      PreparedStatement preStatement = null;
      ResultSet resultSet = null;
      String baseEntityId = null;
      try {
        driver = (RDBMSAbstractDriver) RDBMSDriverManager.getDriver();
        connection = DataSourceUtils.getConnection(driver.getDataSource());
        
        Map<String, Long> propertyConfig = new HashMap<String, Long>();
        fetchPropertyConfig(propertyConfig, connection);
        
        String sql = "select * from staging.\"assets\" order by \"id\" asc offset ? limit ?;";
        preStatement = connection.prepareStatement(sql);
        preStatement.setInt(1, offset);
        preStatement.setInt(2, limit);
        resultSet = preStatement.executeQuery();
        
        while (resultSet.next()) {
          baseEntityId = resultSet.getString(1);
          uploadAssetOnServer(baseEntityId, Arrays.asList((String[]) resultSet.getArray(2).getArray()), resultSet.getString(3),
              resultSet.getBoolean(4), assetExtensions, metadataPropertyMapping, propertyConfig, connection);
        }
        
        resultSet.close();
        preStatement.close();
        connection.commit();
        connection.close();
      }
      catch (Exception e) {
        log("Batch failed, batch id:" + (offset / batchSize), FAILURE_LOGS, e);
      }
      finally {
        try {
          resultSet.close();
        }
        catch (Exception e) {
          /* Ignored */ }
        try {
          preStatement.close();
        }
        catch (Exception e) {
          /* Ignored */ }
        try {
          connection.close();
        }
        catch (Exception e) {
          /* Ignored */ }
      }
    }
  }
  
  /**
   * @param propertyConfig
   * @param connection
   * @throws Exception
   */
  private void fetchPropertyConfig(Map<String, Long> propertyConfig, Connection connection) throws Exception
  {
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery(
        "SELECT * from pxp.propertyconfig where propertycode IN ('I_META_FILE_NAME','I_MODIFICATION_DATE','I_META_FILE_SIZE','I_DIMENSIONS','I_RESOLUTION','assetdownloadcountattribute');");
    while (resultSet.next()) {
      propertyConfig.put(resultSet.getString(2), resultSet.getLong(1));
    }
    
    resultSet.close();
    statement.close();
  }
  
  /**
   * @param baseEntityId
   * @param natureKlasses
   * @param filePath
   * @param isProcessed
   * @param assetExtensions
   * @param metadataPropertyMapping
   * @param propertyConfig
   * @param connection
   * @return
   * @throws Exception
   */
  private void uploadAssetOnServer(String baseEntityId, List<String> natureKlasses, String filePath, boolean isProcessed,
      Map<String, List<String>> assetExtensions, Map<String, Object> metadataPropertyMapping, Map<String, Long> propertyConfig,
      Connection connection) throws Exception
  {
    try {
      if (isProcessed) {
        return;
      }
      Long baseEntityIId = fetchBaseEntityIID(baseEntityId, connection);
      if (baseEntityIId == null) {
        log("BaseEntity IId is not found: " + baseEntityId, FAILURE_LOGS, null);
        return;
      }
      Map<String, Object> assetMap = uploadFileToSwiftServer(filePath, assetExtensions, connection, natureKlasses.get(0));
      // Update base entity
      if (assetMap != null) {
        Map<String, Object> entityExtension = prepareAssetExtenstionData(assetMap);
        updateBaseEntity(baseEntityIId, ObjectMapperUtil.writeValueAsString(entityExtension), (String) assetMap.get("hash"), connection);
        Map<String, Object> metadataMap = (Map<String, Object>) assetMap.get("metadata");
        Map<String, String> meta = convertAssetMetadataToMap(metadataMap, metadataPropertyMapping);
        for (String attrCode : METADATA_PROPERTIES_TO_UPDATE) {
          String value = meta.get(attrCode);
          if (attrCode.equals("assetdownloadcountattribute")) {
            value = "0";
          }
          if (attrCode.equals("I_META_FILE_NAME")) {
            value = (String) metadataMap.get("name");
          }
          if (value != null && !value.isEmpty()) {
            createValueRecord(baseEntityIId, propertyConfig.get(attrCode), value, attrCode == "I_RESOLUTION" ? Long.valueOf(value) : 0l,
                connection);
          }
        }
        updateStaging(baseEntityId, connection);
        log("Upload success for base entity id:" + baseEntityId, SUCCESS_LOGS, null);
      }
    }
    catch (Exception e) {
      log("Uplaod failed for baseEntityId: " + baseEntityId, FAILURE_LOGS, e);
    }
  }
  
  /**
   * @param baseEntityId
   * @param connection
   * @return
   * @throws Exception
   */
  private Long fetchBaseEntityIID(String baseEntityId, Connection connection) throws Exception
  {
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery("SELECT baseentityiid from pxp.baseentity where baseentityid = '" + baseEntityId + "';");
    while (resultSet.next()) {
      return resultSet.getLong(1);
    }
    resultSet.close();
    statement.close();
    
    return null;
  }
  
  /**
   * @param klassId
   * @param fileSource
   * @param assetExtensions
   * @param connection
   * @return
   * @throws Exception
   */
  protected Map<String, Object> uploadFileToSwiftServer(String fileSource, Map<String, List<String>> assetExtensions, Connection connection, String natureKlass)
      throws Exception
  {
    String extension = FilenameUtils.getExtension(fileSource);
    String eligibleAssetExtensionType = checkEligibleAssetExtensionTypes(extension, assetExtensions);
    if (eligibleAssetExtensionType == null) {
      throw new Exception("Extension not supported " + extension);
    }
    Map<String, Object> assetMap = uploadAssetToServer(fileSource, connection, natureKlass);
    assetMap.put("assetUploadType", eligibleAssetExtensionType);
    
    return assetMap;
  }
  

  /**
   * @param filePath
   * @param connection
   * @param natureKlass
   * @return
   * @throws Exception
   */
  private Map<String, Object> uploadAssetToServer(String filePath, Connection connection, String natureKlass) throws Exception
  {
    String folderName = null;
    try {
      URL url = new URL(filePath);
      String fileName = FilenameUtils.getName(url.getPath());
      folderName = downloadFile(url, "", fileName);
      filePath = folderName + fileName;
      
      // Metadata
      Map<String, Object> assetMetadataAttributes = ExtractMetadata.extractMetadataFromFile(filePath);
      assetMetadataAttributes.put("name", fileName);
      
      // Get Auth info
      IJSONContent authenticateSwiftServer = CSDAMServer.instance().getServerInformation();
      String storagePath = authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_STORAGE_URL, "");
      String authToken = authenticateSwiftServer.getInitField(CSDAMServer.RESPONSE_HEADER_AUTH_TOKEN, "");
      
      // Hash
      String hash = AssetUtils.hashFile(filePath);
      
      switch (natureKlass) {
        case DAMConstants.IMAGE_KLASS:
          return processImageAsset(filePath, connection, fileName, hash, assetMetadataAttributes, storagePath, authToken);
        case DAMConstants.DOCUMENT_KLASS:
          return processDocumentAsset(filePath, connection, fileName, hash, assetMetadataAttributes, storagePath, authToken);
        case DAMConstants.VIDEO_KLASS:
          return processMediaAsset(filePath, connection, fileName, hash, assetMetadataAttributes, storagePath, authToken);
        default:
          break;
      }
      
      return null;
    }
    catch (Exception e) {
      throw e;
    }
    finally {
      if (StringUtils.isNotBlank(folderName)) {
        deleteDownloadedFile(folderName);
      }
    }
  }
  
  /**
   * @param filePath
   * @param connection
   * @param fileName
   * @param hash
   * @param assetMetadataAttributes
   * @param authToken
   * @param storagePath
   * @return
   * @throws Exception
   */
  private Map<String, Object> processImageAsset(String filePath, Connection connection, String fileName, String hash,
      Map<String, Object> assetMetadataAttributes, String storagePath, String authToken) throws Exception
  {
    Map<String, Object> fileModel = getBasicInformationOfFile(filePath, connection);
    String extension = (String) fileModel.get(EXTENSION);
    String imageType = DAMConstants.TYPE_ORIGINAL;
    String container = DAMConstants.SWIFT_CONTAINER_IMAGE;
    Map<String, Integer> imageDimensions = new HashMap<>();
    
    try {
      imageDimensions = ImageConverter.getImageDimensions(filePath);
      imageType = uploadThumbnail(fileModel, filePath, container, imageType, authToken, storagePath, imageDimensions);
    }
    catch (ImageMagickException e) {
      throw e;
    }
    
    /** * ORIGINAL ** */
    Map<String, Object> originalUploadDataModel = AssetUtils.fillUploadCommonData(fileModel, container, extension, authToken, storagePath,
        imageType, DAMConstants.CONTENT_TYPE_IMAGE);
    
    try (InputStream targetStream = new FileInputStream(new File(filePath))) {
      byte[] bytes = IOUtils.toByteArray(targetStream);
      originalUploadDataModel.put("assetBytes", bytes);
    }
    
    ((Map<String, Object>) originalUploadDataModel.get(ASSET_DATA)).put(DAMConstants.REQUEST_HEADER_OBJECT_META_THUMB,
        fileModel.get(THUMB_KEY));
    originalUploadDataModel.put(ASSET_OBJECT_KEY, fileModel.get(ASSET_OBJECT_KEY));
    
    CSDAMServer.instance().uploadAsset(originalUploadDataModel);
    return fillReturnModel(assetMetadataAttributes, hash, fileName, extension, fileModel, imageDimensions, filePath, "Image");
  }
  
  /**
   * @param filePath
   * @param connection
   * @param fileName
   * @param hash
   * @param assetMetadataAttributes
   * @param storagePath
   * @param authToken
   * @return
   * @throws Exception
   */
  private Map<String, Object> processDocumentAsset(String filePath, Connection connection, String fileName, String hash,
      Map<String, Object> assetMetadataAttributes, String storagePath, String authToken) throws Exception
  {
    Map<String, Object> fileModel = getBasicInformationOfFile(filePath, connection);
    String extension = (String) fileModel.get(EXTENSION);
    String container = DAMConstants.SWIFT_CONTAINER_DOCUMENT;
    boolean isFilePDF = FileExtensionConstants.PDF.equalsIgnoreCase(extension);
    String pdfFilePath = isFilePDF ? filePath : "";
    String previewKey = newUniqueID(BaseType.ASSET.getPrefix(), connection);
    fileModel.put(PREVIEW_KEY, "");
    
    Boolean isPasswordProtected = isFilePDF && (assetMetadataAttributes.get("PDF:Encryption") != null);
    BufferedImage documentThumb = null;
    
    try {
      if (!isFilePDF) {
        pdfFilePath = DocumentConverter.convertDocument(filePath, false);
      }
      // cannot generate thumbnail for password protected file.
      if (Boolean.FALSE.equals(isPasswordProtected)) {
        documentThumb = DocumentConverter.convertPdf(pdfFilePath);
      }
    }
    catch (Exception e) {
      throw e;
    }
    
    /** Thumbnail Processing **/
    if (documentThumb != null) {
      String thumbFileFormat = FileExtensionConstants.PNG;
      Map<String, Object> thumbUploadDataModel = fillUploadData(fileModel, container, thumbFileFormat, authToken, storagePath,
          DAMConstants.TYPE_THUMBNAIL, DAMConstants.CONTENT_TYPE_IMAGE);
      thumbUploadDataModel.put("assetBytes", imageToBytes(documentThumb, thumbFileFormat));
      
      CSDAMServer.instance().uploadAsset(thumbUploadDataModel);
    }
    
    /** * PDF For Preview ** */
    if (!pdfFilePath.isEmpty()) {
      fileModel.put(PREVIEW_KEY, previewKey);
      String previewFileFormat = FileExtensionConstants.PDF;
      File pdfFile = null;
      try {
        pdfFile = new File(pdfFilePath);
        Map<String, Object> previewDataModel = fillUploadData(fileModel, container, previewFileFormat, authToken, storagePath,
            DAMConstants.TYPE_PREVIEW, DAMConstants.CONTENT_TYPE_APPLICATION);
        if (pdfFile != null) {
          previewDataModel.put("assetBytes", AssetUtils.getBytesFromFile(pdfFile));
        }
        CSDAMServer.instance().uploadAsset(previewDataModel);
      }
      finally {
        if (pdfFile.exists() && !isFilePDF) {
          AssetUtils.deleteFileAndDirectory(pdfFile);
        }
      }
    }
    
    /** * ORIGINAL ** */
    Map<String, Object> uploadDataModel = fillUploadData(fileModel, container, extension, authToken, storagePath,
        DAMConstants.TYPE_ORIGINAL, DAMConstants.CONTENT_TYPE_APPLICATION);
    
    try (InputStream targetStream = new FileInputStream(new File(filePath))) {
      byte[] bytes = IOUtils.toByteArray(targetStream);
      uploadDataModel.put("assetBytes", bytes);
    }
    
    CSDAMServer.instance().uploadAsset(uploadDataModel);
    
    return fillReturnModel(assetMetadataAttributes, hash, fileName + "." + extension, extension, fileModel, null, null, "Document");
  }
  
  /**
   * @param filePath
   * @param connection
   * @param fileName
   * @param hash
   * @param assetMetadataAttributes
   * @param storagePath
   * @param authToken
   * @return
   * @throws Exception
   */
  private Map<String, Object> processMediaAsset(String filePath, Connection connection, String fileName, String hash,
      Map<String, Object> assetMetadataAttributes, String storagePath, String authToken) throws Exception
  {
    Map<String, Object> fileModel = getBasicInformationOfFile(filePath, connection);
    String extension = (String) fileModel.get(EXTENSION);
    String container = DAMConstants.SWIFT_CONTAINER_VIDEO;
    Boolean isFileMp4 = extension.equalsIgnoreCase(FileExtensionConstants.MP4);
    
    String mp4Key = Boolean.TRUE.equals(isFileMp4) ? (String) fileModel.get(ASSET_OBJECT_KEY)
        : RDBMSAppDriverManager.getDriver().newUniqueID(BaseType.ASSET.getPrefix());
    fileModel.put(MP4_KEY, mp4Key);
    
    try {
      /** Thumbnail **/
      uploadThumbnail(fileModel, filePath, storagePath, authToken, container);
      /** MP4 Preview **/
      uploadMp4(filePath, storagePath, authToken, isFileMp4, container, fileModel);
    }
    catch (FFMPEGException e) {
      throw e;
    }
    
    /** Original **/
    Map<String, Object> originalVideoUploadModel = AssetUtils.fillUploadCommonData(fileModel, container, extension, authToken, storagePath,
        DAMConstants.TYPE_ORIGINAL, DAMConstants.CONTENT_TYPE_VIDEO);
    
    try (InputStream targetStream = new FileInputStream(new File(filePath))) {
      byte[] bytes = IOUtils.toByteArray(targetStream);
      originalVideoUploadModel.put("assetBytes", bytes);
    }
    
    ((Map<String, Object>) originalVideoUploadModel.get(ASSET_DATA)).put(DAMConstants.REQUEST_HEADER_OBJECT_META_THUMB,
        fileModel.get(THUMB_KEY));
    originalVideoUploadModel.put(DAMConstants.REQUEST_HEADER_OBJECT_META_MP4_ID, fileModel.get(MP4_KEY));
    originalVideoUploadModel.put(ASSET_OBJECT_KEY, fileModel.get(ASSET_OBJECT_KEY));
    
    CSDAMServer.instance().uploadAsset(originalVideoUploadModel);
    
    return fillReturnModel(assetMetadataAttributes, hash, fileName + "." + extension, extension, fileModel, null, null, "Video");
  }
  
  /**
   * @param fileSourcePath
   * @param storagePath
   * @param authToken
   * @param isFileMp4
   * @param container
   * @param fileModel
   * @throws IOException
   * @throws FFMPEGException
   * @throws CSInitializationException
   * @throws PluginException
   */
  private void uploadMp4(String fileSourcePath, String storagePath, String authToken, Boolean isFileMp4, String container,
      Map<String, Object> fileModel) throws IOException, FFMPEGException, CSInitializationException, PluginException
  {
    if (Boolean.FALSE.equals(isFileMp4)) {
      Map<String, Object> uploadDataModel = AssetUtils.fillUploadCommonData(fileModel, container, FileExtensionConstants.MP4, authToken,
          storagePath, DAMConstants.TYPE_MP4, DAMConstants.CONTENT_TYPE_VIDEO);
      byte[] mp4VideoBytes = VideoAssetConverter.convertVideo(fileSourcePath);
      uploadDataModel.put(ASSET_OBJECT_KEY, fileModel.get(MP4_KEY));
      uploadDataModel.put("assetBytes", mp4VideoBytes);
      
      CSDAMServer.instance().uploadAsset(uploadDataModel);
    }
  }
  
  /**
   * @param fileModel
   * @param fileSourcePath
   * @param storagePath
   * @param authToken
   * @param container
   * @throws Exception
   */
  private void uploadThumbnail(Map<String, Object> fileModel, String fileSourcePath, String storagePath, String authToken, String container)
      throws Exception
  {
    BufferedImage videoThumbnail = VideoAssetConverter.getVideoThumbnail(fileSourcePath);
    String thumbFileFormat = FileExtensionConstants.PNG;
    Map<String, Object> thumbUploadDataModel = fillUploadData(fileModel, container, thumbFileFormat, authToken, storagePath,
        DAMConstants.TYPE_THUMBNAIL, DAMConstants.CONTENT_TYPE_IMAGE);
    thumbUploadDataModel.put("assetBytes", imageToBytes(videoThumbnail, thumbFileFormat));
    
    CSDAMServer.instance().uploadAsset(thumbUploadDataModel);
  }
  
  /**
   * @param metadata
   * @param hash
   * @param name
   * @param extension
   * @param fileModel
   * @param imageDimensions
   * @param fileSourcePath
   * @return
   */
  private Map<String, Object> fillReturnModel(Map<String, Object> metadata, String hash, String name, String extension,
      Map<String, Object> fileModel, Map<String, Integer> imageDimensions, String fileSourcePath, String assetType)
  {
    Map<String, Object> assetMap = new HashMap<>();
    assetMap.put("fileName", name);
    assetMap.put(IAssetKeysModel.IMAGE_KEY, fileModel.get(ASSET_OBJECT_KEY));
    assetMap.put(ASSET_OBJECT_KEY, fileModel.get(ASSET_OBJECT_KEY));
    assetMap.put(IAssetImageKeysModel.THUMB_KEY, fileModel.get(THUMB_KEY));
    assetMap.put("hash", hash);
    assetMap.put("metadata", metadata);
    assetMap.put(EXTENSION, extension);
    assetMap.put("type", assetType);
    
    if (imageDimensions != null) {
      assetMap.put(DAMConstants.WIDTH, imageDimensions.get(DAMConstants.WIDTH));
      assetMap.put(DAMConstants.HEIGHT, imageDimensions.get(DAMConstants.HEIGHT));
      assetMap.put(DAMConstants.THUMBNAIL_PATH, fileModel.get(DAMConstants.THUMBNAIL_PATH));
    }
    
    switch (assetType) {
      case "Video":
        assetMap.put(IAssetVideoKeysModel.MP4_KEY, fileModel.get("mp4Key"));
        break;
      case "Document":
        assetMap.put(IAssetDocumentKeysModel.PREVIEW_KEY, fileModel.get(PREVIEW_KEY));
        break;
      case "Image":
        assetMap.put(IAssetImageKeysModel.FILE_PATH, fileSourcePath);
        break;
    }
    
    return assetMap;
  }
  
  /**
   * @param fileSourcePath
   * @param connection
   * @return
   * @throws RDBMSException
   */
  protected Map<String, Object> getBasicInformationOfFile(String fileSourcePath, Connection connection) throws Exception
  {
    Map<String, Object> basicInfoMap = new HashMap<>();
    basicInfoMap.put(EXTENSION, FilenameUtils.getExtension(fileSourcePath));
    basicInfoMap.put(NAME, fileSourcePath);
    basicInfoMap.put(ASSET_OBJECT_KEY, newUniqueID(BaseType.ASSET.getPrefix(), connection));
    basicInfoMap.put(THUMB_KEY, newUniqueID(BaseType.ASSET.getPrefix(), connection));
    
    return basicInfoMap;
  }
  
  /**
   * @param fileModel
   * @param fileSourcePath
   * @param container
   * @param imageType
   * @param authToken
   * @param storagePath
   * @param imageDimensions
   * @return
   * @throws Exception
   */
  private String uploadThumbnail(Map<String, Object> fileModel, String fileSourcePath, String container, String imageType, String authToken,
      String storagePath, Map<String, Integer> imageDimensions) throws Exception
  {
    String extension = (String) fileModel.get(EXTENSION);
    String thumbnailDirectoryPath = null;
    try {
      if (isThumbRequired(imageDimensions, extension)) {
        String fileFormat = extension.equals(FileExtensionConstants.JPG) || extension.equals(FileExtensionConstants.JPEG) ? extension
            : FileExtensionConstants.PNG;
        
        BufferedImage thumbImage = null;
        
        thumbnailDirectoryPath = AssetUtils.getFilePath() + UUID.randomUUID().toString();
        String thumbFileName = DAMConstants.TEMP_CONVERSION_FILE_NAME_PREFIX + "temp" + FileExtensionConstants.PNG_EXT;
        String thumbImagePath = thumbnailDirectoryPath + File.separator + thumbFileName;
        File directory = new File(thumbnailDirectoryPath);
        directory.mkdirs();
        thumbImage = ImageConverter.convertImage(fileSourcePath, thumbImagePath);
        fileModel.put(DAMConstants.THUMBNAIL_PATH, thumbImagePath);
        
        Map<String, Object> thumbUploadDataModel = fillUploadData(fileModel, container, fileFormat, authToken, storagePath,
            DAMConstants.TYPE_THUMBNAIL, DAMConstants.CONTENT_TYPE_IMAGE);
        thumbUploadDataModel.put("assetBytes", imageToBytes(thumbImage, fileFormat));
        
        CSDAMServer.instance().uploadAsset(thumbUploadDataModel);
      }
      else {
        fileModel.put(THUMB_KEY, fileModel.get(ASSET_OBJECT_KEY));
        imageType = DAMConstants.TYPE_ORIGINAL_THUMBNAIL;
        fileModel.put(DAMConstants.THUMBNAIL_PATH, fileSourcePath);
      }
    }
    catch (Exception e) {
      throw e;
    }
    finally {
      if (StringUtils.isNotBlank(thumbnailDirectoryPath)) {
        deleteDownloadedFile(thumbnailDirectoryPath);
      }
    }
    
    return imageType;
  }
  
  /**
   * @param img
   * @param extension
   * @return
   * @throws IOException
   */
  protected byte[] imageToBytes(BufferedImage img, String extension) throws IOException
  {
    byte[] imageInByte;
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      ImageIO.write(img, extension, baos);
      baos.flush();
      imageInByte = baos.toByteArray();
    }
    return imageInByte;
  }
  
  /**
   * @param fileModel
   * @param container
   * @param fileFormat
   * @param authToken
   * @param storagePath
   * @param metaType
   * @param contentType
   * @return
   */
  protected Map<String, Object> fillUploadData(Map<String, Object> fileModel, String container, String fileFormat, String authToken,
      String storagePath, String metaType, String contentType)
  {
    Map<String, Object> uploadDataModel = AssetUtils.fillUploadCommonData(fileModel, container, fileFormat, authToken, storagePath,
        metaType, contentType);
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
  
  /**
   * @param imageDimensions
   * @param extension
   * @return
   */
  private boolean isThumbRequired(Map<String, Integer> imageDimensions, String extension)
  {
    if (FileExtensionConstants.AI.equals(extension) || FileExtensionConstants.EPS.equals(extension)
        || FileExtensionConstants.PSD.equals(extension) || FileExtensionConstants.ICO.equals(extension)) {
      return true;
    }
    return (imageDimensions.get(DAMConstants.HEIGHT) > 200 || imageDimensions.get(DAMConstants.WIDTH) > 200);
  }
  
  /**
   * @param assetMap
   * @return
   * @throws Exception
   */
  private Map<String, Object> prepareAssetExtenstionData(Map<String, Object> assetMap) throws Exception
  {
    Map<String, Object> imageAttr = new HashMap<>();
    Map<String, Object> properties = new HashMap<>();
    String assetUploadType = (String) assetMap.get("assetUploadType");
    imageAttr.put(IImageAttributeInstance.FILENAME, (String) assetMap.get("fileName"));
    if (DOCUMENT_ASSET.equals(assetUploadType)) {
      imageAttr.put(IImageAttributeInstance.PREVIEW_IMAGE_KEY, (String) assetMap.get("previewKey"));
    }
    else if (IMAGE_ASSET.equals(assetUploadType)) {
      properties.put(WIDTH, assetMap.get("width").toString());
      properties.put(HEIGHT, assetMap.get("height").toString());
    }
    else if (VIDEO_ASSET.equals(assetUploadType)) {
      properties.put("mp4", (String) assetMap.get("mp4Key"));
      imageAttr.put(IImageAttributeInstance.PREVIEW_IMAGE_KEY, (String) assetMap.get("mp4Key"));
    }
    properties.put(STATUS, "0");
    properties.put(EXTENSION, (String) assetMap.get(EXTENSION));
    imageAttr.put(IImageAttributeInstance.ASSET_OBJECT_KEY, (String) assetMap.get("imageKey"));
    imageAttr.put(IImageAttributeInstance.PROPERTIES, properties);
    imageAttr.put(IImageAttributeInstance.THUMB_KEY, (String) assetMap.get("thumbKey"));
    imageAttr.put(IImageAttributeInstance.TYPE, getAssetType(assetUploadType));
    
    return imageAttr;
  }
  
  /**
   * @param assetUploadType
   * @return
   */
  private static String getAssetType(String assetUploadType)
  {
    switch (assetUploadType) {
      case IMAGE_ASSET:
        return CommonConstants.SWIFT_CONTAINER_IMAGE;
      case VIDEO_ASSET:
        return CommonConstants.SWIFT_CONTAINER_VIDEO;
      case DOCUMENT_ASSET:
        return CommonConstants.SWIFT_CONTAINER_DOCUMENT;
    }
    return null;
  }
  
  /**
   * @param url
   * @param destinationPath
   * @param fileName
   * @return
   * @throws Exception
   */
  private static String downloadFile(URL url, String destinationPath, String fileName) throws Exception
  {
    String folderName = new File(destinationPath).getAbsolutePath() + File.separator + UUID.randomUUID().toString() + File.separator;
    new File(folderName).mkdir();
    String destName = folderName + fileName;
    try {
      downloadFromURL(url, destName);
    }
    catch (Exception e) {
      if (StringUtils.isNotBlank(folderName)) {
        deleteDownloadedFile(folderName);
      }
      throw e;
    }
    return folderName;
  }
  
  /**
   * Download file from URL
   * 
   * @param url
   * @param file
   * @throws Exception
   */
  private static void downloadFromURL(URL url, String file) throws Exception
  {
    try (ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        FileOutputStream fileOutStream = new FileOutputStream(file)) {
      fileOutStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
    }
    catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * @param folderName
   * @throws IOException
   */
  private static void deleteDownloadedFile(String folderName) throws IOException
  {
    File file = new File(folderName);
    FileUtils.deleteDirectory(file);
  }
  
  /**
   * @param multiPartFileInfoModel
   * @param assetExtensions
   * @return
   * @throws Exception
   */
  private String checkEligibleAssetExtensionTypes(String originalExtension, Map<String, List<String>> assetExtensions) throws Exception
  {
    String extension = "." + originalExtension;
    for (Entry<String, List<String>> eligibleExtensionTypes : assetExtensions.entrySet()) {
      if (eligibleExtensionTypes.getValue().contains(extension)) {
        return eligibleExtensionTypes.getKey();
      }
    }
    return null;
  }
  
  /**
   * @param prefix
   * @param connection
   * @return
   * @throws Exception
   */
  private String newUniqueID(String prefix, Connection connection) throws Exception
  {
    String sql = "SELECT * FROM pxp.fn_createuniqueid (?)";
    PreparedStatement pstmt = connection.prepareStatement(sql);
    pstmt.setString(1, prefix);
    ResultSet result = pstmt.executeQuery();
    while (result.next()) {
      return result.getString(1);
    }
    
    return null;
  }
  
  /**
   * @param baseEntityId
   * @param connection
   * @return
   * @throws Exception
   */
  private void updateStaging(String baseEntityId, Connection connection) throws Exception
  {
    String updateSql = "UPDATE staging.\"assets\" SET \"isProcessed\" = true WHERE id = ?;";
    PreparedStatement pstmt = connection.prepareStatement(updateSql);
    pstmt.setString(1, baseEntityId);
    pstmt.executeUpdate();
    
    pstmt.close();
  }
  
  /**
   * @param baseEntityIId
   * @param entityExtension
   * @param hash
   * @param connection
   * @return
   * @throws Exception
   */
  private void updateBaseEntity(Long baseEntityIId, String entityExtension, String hash, Connection connection) throws Exception
  {
    String updateSql = "UPDATE pxp.\"baseentity\" SET entityextension = ?::jsonb, hashcode = ? WHERE baseentityiid = ?;";
    PreparedStatement pstmt = connection.prepareStatement(updateSql);
    pstmt.setObject(1, entityExtension);
    pstmt.setString(2, hash);
    pstmt.setLong(3, baseEntityIId);
    pstmt.executeUpdate();
    
    pstmt.close();
  }
  
  /**
   * @param baseEntityId
   * @param propertyIId
   * @param value
   * @param number
   * @param connection
   * @return
   * @throws Exception
   */
  private void createValueRecord(Long baseEntityIId, Long propertyIId, String value, long number, Connection connection) throws Exception
  {
    String updateSql = "INSERT INTO pxp.valuerecord(propertyiid, entityiid, recordstatus, value, ashtml, asnumber, unitsymbol, calculation) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    PreparedStatement pstmt = connection.prepareStatement(updateSql);
    pstmt.setLong(1, propertyIId);
    pstmt.setLong(2, baseEntityIId);
    pstmt.setInt(3, 1);
    pstmt.setString(4, value);
    pstmt.setString(5, "");
    pstmt.setLong(6, number);
    pstmt.setString(7, "");
    pstmt.setString(8, "");
    pstmt.executeUpdate();
    
    pstmt.close();
  }
  
  /**
   * @param metadataMap
   * @param metadataPropertyMapping
   * @return
   * @throws Exception
   */
  private Map<String, String> convertAssetMetadataToMap(Map<String, Object> metadataMap, Map<String, Object> metadataPropertyMapping)
      throws Exception
  {
    Map<String, String> assetMetadataAttributes = new HashMap<>();
    Map<String, Object> propertyMap = (Map<String, Object>) metadataPropertyMapping.get(ITransformationTask.PROPERTY_MAP);
    List<String> priorityList = (List<String>) metadataPropertyMapping.get(ITransformationTask.PRIORITY);
    Map<String, Object> convertedMap = MetadataUtils.convertMetadataIntoMap(metadataMap, priorityList);
    for (Map.Entry<String, Object> entry : propertyMap.entrySet()) {
      String attributeId = entry.getKey();
      Map<String, Object> mapping = (Map<String, Object>) entry.getValue();
      String finalValue = null;
      for (String metadataKey : priorityList) {
        List<String> keyList = (List<String>) mapping.get(metadataKey);
        Map<String, Object> metadataKeyMap = (Map<String, Object>) convertedMap.get(metadataKey);
        if (metadataKeyMap != null && keyList != null) {
          for (String key : keyList) {
            finalValue = (String) metadataKeyMap.get(key);
            if (finalValue != null && !finalValue.equals("")) {
              assetMetadataAttributes.put(attributeId, finalValue);
              break;
            }
          }
        }
      }
    }
    return assetMetadataAttributes;
  }
  
  /**
   * @param message
   * @param fileName
   * @param throwable
   */
  private static void log(String message, String fileName, Throwable throwable)
  {
    try {
      DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
      File logFile = new File("assetUploadLogs" + File.separator + fileName + "_" + dateFormat.format(LocalDateTime.now()) + ".txt");
      File logDirectory = logFile.getParentFile();
      if (!logDirectory.exists()) {
        logDirectory.mkdirs();
      }
      
      FileWriter writer = new FileWriter(logFile, true);
      String logString = message + "\n";
      if (throwable != null) {
        logString = logString + throwable.getMessage() + "\n" + ExceptionUtils.getStackTrace(throwable) + "\n";
      }
      writer.append(logString);
      writer.flush();
      writer.close();
    }
    catch (IOException e) {
      // Do nothing.
    }
  }
}