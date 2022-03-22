package com.cs.core.config.interactor.entity.governancerule;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeyPerformanceIndicator implements IKeyPerformanceIndicator {
  
  private static final long     serialVersionUID = 1L;
  protected String              id;
  protected String              label;
  protected Long                versionId;
  protected Long                versionTimestamp;
  protected String              lastModifiedBy;
  protected String              frequency;
  protected ITargetFilters      targetFilters;
  protected List<IKPITag>       kpiTags;
  protected List<String>        rules;
  protected Map<String, Object> roles;
  protected List<IDrillDown>    drillDowns;
  protected String              code;
  protected String              dashboardTabId;
  protected List<String>        organizations;
  protected List<String>        physicalCatalogIds;
  protected List<String>        endpoints;
  
  @Override
  public String getFrequency()
  {
    return frequency;
  }
  
  @Override
  public void setFrequency(String frequency)
  {
    this.frequency = frequency;
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
  public ITargetFilters getTargetFilters()
  {
    return targetFilters;
  }
  
  @JsonDeserialize(as = TargetFilters.class)
  @Override
  public void setTargetFilters(ITargetFilters targetFilters)
  {
    this.targetFilters = targetFilters;
  }
  
  @Override
  public List<IKPITag> getKpiTags()
  {
    return kpiTags;
  }
  
  @JsonDeserialize(contentAs = KPITag.class)
  @Override
  public void setKpiTags(List<IKPITag> kpiTags)
  {
    this.kpiTags = kpiTags;
  }
  
  @Override
  public List<String> getRules()
  {
    return rules;
  }
  
  @Override
  public void setRules(List<String> rules)
  {
    this.rules = rules;
  }
  
  @Override
  public Map<String, Object> getRoles()
  {
    return roles;
  }
  
  @Override
  public void setRoles(Map<String, Object> roles)
  {
    this.roles = roles;
  }
  
  @Override
  public List<IDrillDown> getDrillDowns()
  {
    return drillDowns;
  }
  
  @JsonDeserialize(contentAs = DrillDown.class)
  @Override
  public void setDrillDowns(List<IDrillDown> drillDowns)
  {
    this.drillDowns = drillDowns;
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
  public List<String> getOrganizations()
  {
    return organizations;
  }
  
  @Override
  public void setOrganizations(List<String> organizations)
  {
    this.organizations = organizations;
  }
  
  @Override
  public List<String> getPhysicalCatalogIds()
  {
    if (physicalCatalogIds == null) {
      physicalCatalogIds = new ArrayList<>();
    }
    return physicalCatalogIds;
  }
  
  @Override
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds)
  {
    this.physicalCatalogIds = physicalCatalogIds;
  }
  
  @Override
  public List<String> getEndpoints()
  {
    if (endpoints == null) {
      endpoints = new ArrayList<>();
    }
    return endpoints;
  }
  
  @Override
  public void setEndpoints(List<String> endpoints)
  {
    this.endpoints = endpoints;
  }
}
