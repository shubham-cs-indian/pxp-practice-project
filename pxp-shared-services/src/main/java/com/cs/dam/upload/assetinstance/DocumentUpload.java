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
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.services.CSDAMServer;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.asset.exceptions.DocumentConversionException;
import com.cs.dam.asset.exceptions.ImageMagickException;
import com.cs.dam.asset.processing.DocumentConverter;
import com.cs.utils.ExceptionUtil;
import com.cs.utils.dam.AssetUtils;
import com.itextpdf.text.ExceptionConverter;

public class DocumentUpload extends AbstractUpload {
  
  private static DocumentUpload instance = null;
  
  private DocumentUpload() {
    // private constructor for Singleton class.
  }
  
  /**
   * Use this method to get DocumentUpload instance
   * @return DocumentUpload instance
   */
  public static DocumentUpload getInstance() {
    if(instance == null) {
      instance = new DocumentUpload();
    }
    return instance;
  }
  
  @Override
  public Map<String, Object> executeUpload(String fileSourcePath, Map<String, Object> metadata,
      String hash, Boolean extractRendition, String storagePath, String authToken, String klassID,
      Boolean shouldExtractMetadata, String container, IExceptionModel warnings, 
      String thumbnailPath, boolean isInDesignServerEnabled) throws Exception
  {
    String fileName = FilenameUtils.getName(fileSourcePath);
    Map<String, Object> fileModel = getBasicInformationOfFile(fileSourcePath);
    String extension = (String) fileModel.get(EXTENSION);
    container = container != null ? container : DAMConstants.SWIFT_CONTAINER_DOCUMENT;
    boolean isFilePDF = FileExtensionConstants.PDF.equalsIgnoreCase(extension);
    String pdfFilePath = isFilePDF ? fileSourcePath : "";
    String previewKey = RDBMSAppDriverManager.getDriver().newUniqueID( BaseType.ASSET.getPrefix());
    fileModel.put(PREVIEW_KEY, "");
    
    if (Boolean.TRUE.equals(extractRendition)) {
      Boolean isPasswordProtected = isFilePDF && (metadata.get("PDF:Encryption") != null);
      BufferedImage documentThumb = null;
      
      try {
        if (!isFilePDF) {
          pdfFilePath = DocumentConverter.convertDocument(fileSourcePath, isInDesignServerEnabled);
        }
        //cannot generate thumbnail for password protected file.
        if (Boolean.FALSE.equals(isPasswordProtected)) {
          documentThumb = DocumentConverter.convertPdf(pdfFilePath);
        }
      }
      catch (ExceptionConverter | RDBMSException | DocumentConversionException | IOException | ImageMagickException e) {
        ExceptionUtil.addFailureDetailsToFailureObject(warnings, new ImageMagickException(e), null, fileName);
      }
      
      /** Thumbnail Processing **/
      if (documentThumb != null) {
        String thumbFileFormat = FileExtensionConstants.PNG;
        Map<String, Object> thumbUploadDataModel = fillUploadData(fileModel, container,
            thumbFileFormat, authToken, storagePath, DAMConstants.TYPE_THUMBNAIL, DAMConstants.CONTENT_TYPE_IMAGE);
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
          Map<String, Object> previewDataModel = fillUploadData(fileModel, container,
              previewFileFormat, authToken, storagePath, DAMConstants.TYPE_PREVIEW,
              DAMConstants.CONTENT_TYPE_APPLICATION);
          if (pdfFile != null) {
            previewDataModel.put("assetBytes", AssetUtils.getBytesFromFile(pdfFile));
          }
          CSDAMServer.instance()
              .uploadAsset(previewDataModel);
        }
        finally {
          if (pdfFile.exists() && !isFilePDF) {
            AssetUtils.deleteFileAndDirectory(pdfFile);
          }
        }
      }
    }
    
    /** * ORIGINAL ** */
    Map<String, Object> uploadDataModel = fillUploadData(fileModel, container, extension, authToken,
        storagePath, DAMConstants.TYPE_ORIGINAL, DAMConstants.CONTENT_TYPE_APPLICATION);
    
    try (InputStream targetStream = new FileInputStream(new File(fileSourcePath))) {
      byte[] bytes = IOUtils.toByteArray(targetStream);
      uploadDataModel.put("assetBytes", bytes);
    }
    
    CSDAMServer.instance()
        .uploadAsset(uploadDataModel);
    
    return fillReturnModel(metadata, hash, fileName + "." + extension, extension, fileModel,
        klassID, "Document");
  }
  
}
