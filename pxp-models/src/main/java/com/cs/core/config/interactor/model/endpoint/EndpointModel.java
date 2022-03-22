package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.endpoint.Endpoint;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;

import java.util.ArrayList;
import java.util.List;

public class EndpointModel implements IEndpointModel {
  
  private static final long serialVersionUID     = 1L;
  
  protected IEndpoint       entity;
  protected List<String>    processes            = new ArrayList<>();
  protected List<String>    jmsProcesses         = new ArrayList<>();
  protected String          icon;
  protected String          iconKey;
  protected List<String>    mappings             = new ArrayList<>();
  protected List<String>    authorizationMapping = new ArrayList<>();
  
  public EndpointModel()
  {
    this.entity = new Endpoint();
  }
  
  public EndpointModel(IEndpoint profile)
  {
    this.entity = profile;
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
  public IEntity getEntity()
  {
    return entity;
  }
  
  @Override
  public List<String> getProcesses()
  {
    return processes;
  }
  
  @Override
  public void setProcesses(List<String> processes)
  {
    this.processes = processes;
  }
  
  @Override
  public List<String> getJmsProcesses()
  {
    return jmsProcesses;
  }

  @Override
  public void setJmsProcesses(List<String> jmsProcesses)
  {
    this.jmsProcesses = jmsProcesses;
  }

  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.entity.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public String getLabel()
  {
    return entity.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    this.entity.setLabel(label);
  }
  
  @Override
  public String getIndexName()
  {
    return entity.getIndexName();
  }

  @Override
  public void setIndexName(String indexName)
  {
    this.entity.setIndexName(indexName);
  }
  
  @Override
  public Boolean getIsRuntimeMappingEnabled()
  {
    return entity.getIsRuntimeMappingEnabled();
  }
  
  @Override
  public void setIsRuntimeMappingEnabled(Boolean isRuntimeMappingEnabled)
  {
    this.entity.setIsRuntimeMappingEnabled(isRuntimeMappingEnabled);
  }
    
  @Override
  public String getEndpointType()
  {
    return entity.getEndpointType();
  }
  
  @Override
  public void setEndpointType(String endpointType)
  {
    this.entity.setEndpointType(endpointType);
  }
  
  @Override
  public String getSystemId()
  {
    return entity.getSystemId();
  }
  
  @Override
  public void setSystemId(String systemId)
  {
    this.entity.setSystemId(systemId);
  }
  
  @Override
  public String getCode()
  {
    return entity.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    entity.setCode(code);
  }
  
  @Override
  public String getDashboardTabId()
  {
    return entity.getDashboardTabId();
  }
  
  @Override
  public void setDashboardTabId(String dashboardTabId)
  {
    entity.setDashboardTabId(dashboardTabId);
  }
  
  @Override
  public List<String> getPhysicalCatalogs()
  {
    return entity.getPhysicalCatalogs();
  }
  
  @Override
  public void setPhysicalCatalogs(List<String> physicalCatalogs)
  {
    entity.setPhysicalCatalogs(physicalCatalogs);
  }
 
  @Override
  public List<String> getMappings()
  {
    return mappings;
  }
  
  @Override
  public void setMappings(List<String> mappings)
  {
    this.mappings = mappings;
  }
  
  @Override
  public List<String> getAuthorizationMapping()
  {
    return authorizationMapping;
  }

  @Override
  public void setAuthorizationMapping(List<String> authorizationMapping)
  {
   this.authorizationMapping = authorizationMapping;    
  }
}
