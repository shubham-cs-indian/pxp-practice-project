package com.cs.core.config.interactor.entity.organization;

import java.util.ArrayList;
import java.util.List;

public class Organization implements IOrganization {
  
  private static final long serialVersionUID = 1L;
  protected String          label;
  protected String          icon;
  protected String          iconKey;
  protected String          type;
  protected String          id;
  protected List<String>    physicalCatalogs;
  protected List<String>    portals;
  protected List<String>    roleIds;
  protected List<String>    taxonomyIds;
  protected List<String>    klassIds         = new ArrayList<>();
  protected List<String>    endpointIds;
  protected String          code;
  protected Boolean         isStandard       = false;
  protected List<String>    systems;
  
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
    return this.iconKey;
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
  public List<String> getPhysicalCatalogs()
  {
    if (physicalCatalogs == null) {
      physicalCatalogs = new ArrayList<>();
    }
    return physicalCatalogs;
  }
  
  @Override
  public void setPhysicalCatalogs(List<String> physicalCatalogs)
  {
    this.physicalCatalogs = physicalCatalogs;
  }
  
  @Override
  public List<String> getPortals()
  {
    if (portals == null) {
      portals = new ArrayList<>();
    }
    return portals;
  }
  
  @Override
  public void setPortals(List<String> portals)
  {
    this.portals = portals;
  }
  
  @Override
  public List<String> getRoleIds()
  {
    if (roleIds == null) {
      roleIds = new ArrayList<>();
    }
    return roleIds;
  }
  
  @Override
  public void setRoleIds(List<String> roleIds)
  {
    this.roleIds = roleIds;
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
  public List<String> getTaxonomyIds()
  {
    if (taxonomyIds == null) {
      taxonomyIds = new ArrayList<>();
    }
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public List<String> getEndpointIds()
  {
    if (endpointIds == null) {
      endpointIds = new ArrayList<String>();
    }
    return endpointIds;
  }
  
  @Override
  public void setEndpointIds(List<String> endpointIds)
  {
    this.endpointIds = endpointIds;
  }
  
  @Override
  public List<String> getKlassIds()
  {
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
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
  public Boolean getIsStandard()
  {
    return isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }
  
  @Override
  public List<String> getSystems()
  {
    if (systems == null) {
      systems = new ArrayList<>();
    }
    return systems;
  }
  
  @Override
  public void setSystems(List<String> systems)
  {
    this.systems = systems;
  }
}
