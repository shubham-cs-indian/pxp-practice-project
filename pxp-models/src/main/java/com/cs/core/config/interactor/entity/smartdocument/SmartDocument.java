package com.cs.core.config.interactor.entity.smartdocument;

import java.util.ArrayList;
import java.util.List;

public class SmartDocument implements ISmartDocument {
  
  private static final long serialVersionUID   = 1L;
  protected String          label;
  protected String          icon;
  protected String          iconKey;
  protected String          type;
  protected String          code;
  protected String          id;
  protected Long            versionId;
  protected Long            versionTimestamp;
  protected String          lastModifiedBy;
  protected String          rendererLicenceKey;
  protected List<String>    physicalCatalogIds = new ArrayList<>();
  protected String          rendererHostIp;
  protected String          rendererPort;
  
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
  public String getRendererLicenceKey()
  {
    return rendererLicenceKey;
  }
  
  @Override
  public void setRendererLicenceKey(String rendererLicenceKey)
  {
    this.rendererLicenceKey = rendererLicenceKey;
  }
  
  @Override
  public List<String> getPhysicalCatalogIds()
  {
    return physicalCatalogIds;
  }
  
  @Override
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds)
  {
    this.physicalCatalogIds = physicalCatalogIds;
  }
  
  @Override
  public String getRendererHostIp()
  {
    // TODO Auto-generated method stub
    return this.rendererHostIp;
  }
  
  @Override
  public void setRendererHostIp(String rendererHostIp)
  {
    this.rendererHostIp = rendererHostIp;
  }
  
  @Override
  public String getRendererPort()
  {
    return this.rendererPort;
  }
  
  @Override
  public void setRendererPort(String rendererPort)
  {
    this.rendererPort = rendererPort;
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
  
}
