package com.cs.core.runtime.strategy.utils;

import java.util.Arrays;
import java.util.List;

public class DownloadUtils {
  
  public static final String       CONTENT_TYPE_IMAGE       = "image/";
  public static final String       CONTENT_TYPE_VIDEO       = "video/";
  public static final String       CONTENT_TYPE_APPLICATION = "application/";
  public static final List<String> IMAGE_TYPES              = Arrays.asList("jpg", "jpeg", "png",
      "ico", "eps", "ai", "psd");
  public static final List<String> VIDEO_TYPES              = Arrays.asList("wmv", "avi", "mov",
      "flv", "mpeg", "mpg", "mp4");
  
  public static String getContentType(String extension)
  {
    String contentType = "";
    if (IMAGE_TYPES.contains(extension)) { // Image
      contentType = CONTENT_TYPE_IMAGE + extension;
    }
    else if (VIDEO_TYPES.contains(extension)) { // Video
      contentType = CONTENT_TYPE_VIDEO + extension;
    }
    else { // Document
      if (extension.equals("pdf")) {
        contentType = CONTENT_TYPE_APPLICATION + extension;
      }
      else {
        contentType = CONTENT_TYPE_APPLICATION + "octet-stream";
      }
    }
    return contentType;
  }
}
