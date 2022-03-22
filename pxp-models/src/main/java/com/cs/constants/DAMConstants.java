package com.cs.constants;

import java.util.Arrays;
import java.util.List;

/**
 * This is constant file for DAM specific constants.
 * 
 * @author pranav.huchche
 *
 */
public class DAMConstants {
  
  private DAMConstants()
  {
    // Private constructor
  }
  
  public static final Integer      THUMBNAIL_HEIGHT                        = 200;
  public static final Integer      THUMBNAIL_WIDTH                         = 200;
  public static final String       TEMP_CONVERTED_FILE_NAME                = "converted";
  public static final String       TEMP_CONVERSION_FILE_NAME_PREFIX        = "conversion_";
  public static final String       PATH_DELIMINATOR                        = "/";
  public static final String       DOT_SEPERATOR                           = ".";
  
  public static final String       SWIFT_CONTAINER_IMAGE                   = "Image";
  public static final String       SWIFT_CONTAINER_VIDEO                   = "Video";
  public static final String       SWIFT_CONTAINER_DOCUMENT                = "Document";
  public static final String       SWIFT_CONTAINER_SD_TEMPLATES            = "SDTemplates";
  public static final String       HEIGHT                                  = "height";
  public static final String       INDD                                    = "indd";
  public static final String       WIDTH                                   = "width";
  public static final String       EXTENSION                               = "extension";
  public static final String       ZIP                                     = "zip";
  public static final String       XTEX                                    = "xtex";
  
  public static final String       TYPE_ORIGINAL                           = "original";
  public static final String       TYPE_THUMBNAIL                          = "thumbnail";
  public static final String       TYPE_PREVIEW                            = "preview";
  public static final String       TYPE_MP4                                = "mp4";
  public static final String       TYPE_ORIGINAL_THUMBNAIL                 = "original-thumbnail";
  public static final String       CONTENT_TYPE_IMAGE                      = "image/";
  public static final String       CONTENT_TYPE_VIDEO                      = "video/";
  public static final String       CONTENT_TYPE_APPLICATION                = "application/";
  
  public static final String       REQUEST_HEADER_AUTH_TOKEN               = "X-Auth-Token";
  public static final String       REQUEST_HEADER_OBJECT_META_FORMAT       = "X-Object-Meta-Format";
  public static final String       REQUEST_HEADER_OBJECT_META_NAME         = "X-Object-Meta-Name";
  public static final String       REQUEST_HEADER_OBJECT_META_TYPE         = "X-Object-Meta-Type";
  public static final String       REQUEST_HEADER_OBJECT_META_CONTENT_TYPE = "X-Object-Meta-Content-Type";
  public static final String       REQUEST_HEADER_OBJECT_META_ORIGINAL     = "X-Object-Meta-Original";
  public static final String       REQUEST_HEADER_OBJECT_META_THUMB        = "X-Object-Meta-Thumb";
  public static final String       REQUEST_HEADER_OBJECT_META_PREVIEW_ID   = "X-Object-Meta-Preview-Id";
  public static final String       REQUEST_HEADER_OBJECT_META_MP4_ID       = "X-Object-Meta-Mp4-Id";
  public static final String       REQUEST_HEADER_CONTENT_TYPE             = "Content-Type";
  public static final String       RESPONSE_HEADER_STORAGE_URL             = "X-Storage-Url";
  
  public static final String       SWIFT_CONTAINER_ICONS                   = "Icons";
  public static final String       SWIFT_CONTAINER_ATTACHMENT              = "Attachment";
  
  public static final List<String> IMAGE_TYPES                             = Arrays.asList("jpg",
      "jpeg", "png", "ico", "eps", "ai", "psd", "tif", "tiff", "gif");
  public static final List<String> VIDEO_TYPES                             = Arrays.asList("wmv",
      "avi", "mov", "flv", "mpeg", "mpg", "mp4");
  public static final List<String> DOCUMENT_TYPES                          = Arrays.asList("pdf",
      "ppt", "pptx", INDD, "doc", "docx", "xls", "xlsx", "obj", "stp", "fbx", ZIP, XTEX);
  public static final List<String> INDESIGN_FILE_TYPES                     = Arrays.asList("idms",
      "idml", INDD);
  
  // MAM natureTypes
  public static final String       MAM_NATURE_TYPE_IMAGE                   = "imageAsset";
  public static final String       MAM_NATURE_TYPE_VIDEO                   = "videoAsset";
  public static final String       MAM_NATURE_TYPE_DOCUMENT                = "documentAsset";
  public static final String       MAM_NATURE_TYPE_FILE                    = "fileAsset";
  
  public static final String       MODE_CONFIG                             = "config";
  public static final String       MODE_RELATIONSHIP_UPLOAD                = "relationshipUpload";
  public static final String       MODE_RELATIONSHIP_BULK_UPLOAD           = "relationshipBulkUpload";
  public static final String       MODE_ATTACHMENT                         = "Attachment";
  public static final String       MODE_BULK_UPLOAD                        = "bulkUpload";
  public static final String       MODE_SINGLE_UPLOAD                      = "singleUpload";
  
  // Asset class
  public static final String       ASSET_KLASS                             = "asset_asset";
  public static final String       IMAGE_KLASS                             = "image_asset";
  public static final String       VIDEO_KLASS                             = "video_asset";
  public static final String       DOCUMENT_KLASS                          = "document_asset";
  public static final String       FILE_MODEL_PATH                         = "path";
  public static final String       FILE_MODEL_IS_EXTRACTED                 = "isExtracted";
  public static final String       FILE_MODEL_EXT_TYPE                     = "extensionType";
  public static final String       FILE_MODEL_KLASS_ID                     = "klassId";
  public static final String       FILE_MODEL_EXT_CONFIG                   = "extensionConfiguration";
  public static final String       FILE_MODEL_PHYSICAL_CATALOG_ID          = "physicalCatalogId";
  public static final String       FILE_MODEL_ORGANIZATION_ID              = "organizationId";
  public static final String       FILE_MODEL_ENDPOINT_ID                  = "endpointId";
  public static final String       FILE_MODEL_CONTAINER                    = "container";
  public static final String       ASSET_OBJECT_KEY                        = "assetObjectKey";
  public static final String       FILE_NAME                               = "fileName";
  public static final String       NAME                                    = "name";
  public static final String       HASH                                    = "hash";
  public static final String       METADATA                                = "metadata";
  public static final String       IS_INDESIGN_SERVER_ENABLED              = "isInDesignServerEnabled";
  
  public static final String       THUMBNAIL_PATH                          = "thumbnailPath";
  public static final String       FILE_NAME_SEPERATOR                     = "_";
}
