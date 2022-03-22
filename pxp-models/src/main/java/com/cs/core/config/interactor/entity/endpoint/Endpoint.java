package com.cs.core.config.interactor.entity.endpoint;

import java.util.ArrayList;
import java.util.List;

public class Endpoint implements IEndpoint {
  
  private static final long serialVersionUID        = 1L;
  protected String          id;
  protected String          label;
  protected String          indexName;
  protected String          endpointType;
  protected Boolean         isRuntimeMappingEnabled = false;
  protected String          code;
  protected String          systemId;
  protected String          dashboardTabId;
  protected List<String>    physicalCatalogs = new ArrayList<>();
  protected String          icon;
  protected String          iconKey;
  
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
  
  @Override
  public String getLabel()
  {
    
    return label;
  }
  
  @Override
  public String getIndexName()
  {
    
    return indexName;
  }
  
  @Override
  public void setIndexName(String indexName)
  {
    this.indexName = indexName;
  }
  
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
   @Override
  public Boolean getIsRuntimeMappingEnabled()
  {
    return isRuntimeMappingEnabled;
  }
  
  @Override
  public void setIsRuntimeMappingEnabled(Boolean isRuntimeMappingEnabled)
  {
    this.isRuntimeMappingEnabled = isRuntimeMappingEnabled;
  }
  
  @Override
  public String getEndpointType()
  {
    return endpointType;
  }
  
  @Override
  public void setEndpointType(String endpointType)
  {
    this.endpointType = endpointType;
  }
  
  @Override
  public String getSystemId()
  {
    return systemId;
  }
  
  @Override
  public void setSystemId(String systemId)
  {
    this.systemId = systemId;
  }
  
  @Override
  public String getDashboardTabId()
  {
    return dashboardTabId;
  }
  
  @Override
  public void setDashboardTabId(String dashboardTabId)
  {
    this.dashboardTabId = dashboardTabId;
  }
  
  @Override
  public List<String> getPhysicalCatalogs()
  {
    return physicalCatalogs;
  }
  
  @Override
  public void setPhysicalCatalogs(List<String> physicalCatalogs)
  {
    this.physicalCatalogs = physicalCatalogs;
  }
}
