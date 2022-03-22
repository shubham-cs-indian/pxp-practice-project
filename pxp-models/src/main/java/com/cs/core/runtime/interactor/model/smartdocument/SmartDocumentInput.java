package com.cs.core.runtime.interactor.model.smartdocument;

import java.util.HashMap;
import java.util.Map;

public class SmartDocumentInput implements ISmartDocumentInput {
  
  private static final long     serialVersionUID  = 1L;
  protected Map<String, byte[]> cssFileBytes      = new HashMap<>();
  protected Map<String, byte[]> templateFileBytes = new HashMap<>();
  protected Map<String, byte[]> fontFileBytes     = new HashMap<>();
  protected byte[] jsonFileBytes;
  
  @Override
  public Map<String, byte[]> getCssFileBytes()
  {
    return cssFileBytes;
  }
  
  @Override
  public void setCssFileBytes(Map<String, byte[]> cssFileBytes)
  {
    this.cssFileBytes = cssFileBytes;
  }
  
  @Override
  public Map<String, byte[]> getTemplateFileBytes()
  {
    return templateFileBytes;
  }
  
  @Override
  public void setTemplateFileBytes(Map<String, byte[]> templateFileBytes)
  {
    this.templateFileBytes = templateFileBytes;
  }
  
  @Override
  public Map<String, byte[]> getFontFileBytes()
  {
    return fontFileBytes;
  }
  
  @Override
  public void setFontFileBytes(Map<String, byte[]> fontFileBytes)
  {
    this.fontFileBytes = fontFileBytes;
  }

  @Override
  public byte[] getJsonFileBytes()
  {
    return jsonFileBytes;
  }

  @Override
  public void setJsonFileBytes(byte[] jsonFileBytes)
  {
    this.jsonFileBytes = jsonFileBytes;
  }
}
