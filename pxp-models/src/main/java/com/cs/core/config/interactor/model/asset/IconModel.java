package com.cs.core.config.interactor.model.asset;

public class IconModel implements IIconModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          label;
  protected String          code;
  protected String          iconKey;
  protected Long            createdOn;
  protected Long            modifiedOn;
  protected String          versionId;
  
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
  public Long getCreatedOn()
  {
    return createdOn;
  }
  
  @Override
  public void setCreatedOn(Long createdOn)
  {
    this.createdOn = createdOn;
  }
  
  @Override
  public Long getModifiedOn()
  {
    return modifiedOn;
  }
  
  @Override
  public void setModifiedOn(Long modifiedOn)
  {
    this.modifiedOn = modifiedOn;
  }
  
  public String getVersionId()
  {
    return versionId;
  }
  
  public void setVersionId(String versionId)
  {
    this.versionId = versionId;
  }
  
}
