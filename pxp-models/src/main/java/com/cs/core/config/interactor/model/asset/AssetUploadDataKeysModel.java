package com.cs.core.config.interactor.model.asset;

public class AssetUploadDataKeysModel implements IAssetUploadDataKeysModel {
  
  protected String key;
  protected String name;
  protected String format;
  protected String type;
  protected byte[] bytes;
  protected String contentType;
  
  public AssetUploadDataKeysModel(String key, String name, String format, String type, byte[] bytes,
      String contentType)
  {
    this.key = key;
    this.name = name;
    this.format = format;
    this.type = type;
    this.bytes = bytes;
    this.contentType = contentType;
  }
  
  @Override
  public String getKey()
  {
    return key;
  }
  
  @Override
  public void setKey(String key)
  {
    this.key = key;
  }
  
  @Override
  public String getName()
  {
    return name;
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
  
  @Override
  public String getFormat()
  {
    return format;
  }
  
  @Override
  public void setFormat(String format)
  {
    this.format = format;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public byte[] getBytes()
  {
    return bytes;
  }
  
  @Override
  public void setBytes(byte[] bytes)
  {
    this.bytes = bytes;
  }
  
  @Override
  public String getContentType()
  {
    return contentType;
  }
  
  @Override
  public void setContentType(String contentType)
  {
    this.contentType = contentType;
  }
}
