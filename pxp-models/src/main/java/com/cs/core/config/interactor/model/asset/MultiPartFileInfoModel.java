package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;

/**
 * key - Asset Object key
 *
 * <p>
 * originalFilename - Original file name to extracted from MultiPart file
 *
 * <p>
 * bytes - Uploaded file data in bytes
 */
public class MultiPartFileInfoModel extends IdsListParameterModel implements IMultiPartFileInfoModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          key;
  protected String          originalFilename;
  protected byte[]          bytes;
  
  @Override
  public String getOriginalFilename()
  {
    return originalFilename;
  }
  
  @Override
  public void setOriginalFilename(String originalFilename)
  {
    this.originalFilename = originalFilename;
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
  public byte[] getBytes()
  {
    return bytes;
  }
  
  @Override
  public void setBytes(byte[] bytes)
  {
    this.bytes = bytes;
  }
}
