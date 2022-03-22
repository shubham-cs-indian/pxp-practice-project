package com.cs.core.config.interactor.model.asset;

public class AssetExtensionConfiguration implements IAssetExtensionConfiguration {
  
  private static final long serialVersionUID = 1L;
  protected Boolean         extractMetadata  = false;
  protected Boolean         extractRendition = false;
  protected String          extension;
  protected String          id;
  protected String          code;
  
  @Override
  public Boolean getExtractMetadata()
  {
    return extractMetadata;
  }
  
  @Override
  public void setExtractMetadata(Boolean extractMigration)
  {
    this.extractMetadata = extractMigration;
  }
  
  @Override
  public Boolean getExtractRendition()
  {
    return extractRendition;
  }
  
  @Override
  public void setExtractRendition(Boolean extractRendition)
  {
    this.extractRendition = extractRendition;
  }
  
  @Override
  public String getExtension()
  {
    return extension;
  }
  
  @Override
  public void setExtension(String extension)
  {
    this.extension = extension;
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
}
