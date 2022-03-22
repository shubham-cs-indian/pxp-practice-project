package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface ISmartDocumentInput extends IModel {
  
  public static final String CSS_FILE_BYTES      = "cssFileBytes";
  public static final String TEMPLATE_FILE_BYTES = "templateFileBytes";
  public static final String FONTS_FILE_BYTES    = "fontFileBytes";
  public static final String JSON_FILE_BYTES     = "jsonFileBytes";
  
  public Map<String, byte[]> getCssFileBytes();
  public void setCssFileBytes(Map<String, byte[]> cssFileBytes);
  
  public Map<String, byte[]> getTemplateFileBytes();
  public void setTemplateFileBytes(Map<String, byte[]> templateFileBytes);
  
  public Map<String, byte[]> getFontFileBytes();
  public void setFontFileBytes(Map<String, byte[]> fontFileBytes);
  
  public byte[] getJsonFileBytes();
  public void setJsonFileBytes(byte[] jsonFileBytes);
}
