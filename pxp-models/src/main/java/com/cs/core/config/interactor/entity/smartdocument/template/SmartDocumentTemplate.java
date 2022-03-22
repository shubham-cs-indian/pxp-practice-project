package com.cs.core.config.interactor.entity.smartdocument.template;

public class SmartDocumentTemplate implements ISmartDocumentTemplate {
  
  private static final long serialVersionUID = 1L;
  protected String          label;
  protected String          icon;
  protected String          iconKey;
  protected String          type;
  protected String          code;
  protected String          id;
  protected Long            versionId;
  protected Long            versionTimestamp;
  protected String          lastModifiedBy;
  protected String          zipTemplateId;
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getIcon()
  {
    return icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public String getIconKey()
  {
    return iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
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
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public String getZipTemplateId()
  {
    return zipTemplateId;
  }
  
  @Override
  public void setZipTemplateId(String zipAssetId)
  {
    this.zipTemplateId = zipAssetId;
  }
}
