package com.cs.core.config.interactor.entity.sso;

public class SSOConfiguration implements ISSOConfiguration {
  
  private static final long serialVersionUID = 1L;
  
  protected String          code;
  protected String          id;
  protected String          domain;
  protected String          type;
  protected String          idp;
  
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
  
  public String getDomain()
  {
    return domain;
  }
  
  public void setDomain(String domain)
  {
    this.domain = domain;
  }
  
  public String getIdp()
  {
    return idp;
  }
  
  public void setIdp(String idp)
  {
    this.idp = idp;
  }
  
  @Override
  public String getDescription()
  {
    return null;
  }
  
  @Override
  public void setDescription(String description)
  {
  }
  
  @Override
  public String getTooltip()
  {
    return null;
  }
  
  @Override
  public void setTooltip(String tooltip)
  {
  }
  
  @Override
  public Boolean getIsMandatory()
  {
    return null;
  }
  
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return null;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
  }
  
  @Override
  public String getPlaceholder()
  {
    return null;
  }
  
  @Override
  public void setPlaceholder(String placeholder)
  {
  }
  
  @Override
  public String getLabel()
  {
    return null;
  }
  
  @Override
  public void setLabel(String label)
  {
  }
  
  @Override
  public String getIcon()
  {
    return null;
  }
  
  @Override
  public void setIcon(String icon)
  {
  }
  
  @Override
  public String getIconKey()
  {
    return null;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
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
  public Long getVersionId()
  {
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
}
