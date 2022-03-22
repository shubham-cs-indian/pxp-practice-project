package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.config.interactor.entity.governancerule.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public class KeyPerformanceIndicatorModel implements IKeyPerformanceIndicatorModel {
  
  private static final long          serialVersionUID = 1L;
  protected IKeyPerformanceIndicator entity;
  
  public KeyPerformanceIndicatorModel()
  {
    this.entity = new KeyPerformanceIndicator();
  }
  
  public KeyPerformanceIndicatorModel(IKeyPerformanceIndicator entity)
  {
    this.entity = entity;
  }
  
  @Override
  public String getLabel()
  {
    return this.entity.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    this.entity.setLabel(label);
  }
  
  @Override
  public String getFrequency()
  {
    return this.entity.getFrequency();
  }
  
  @Override
  public void setFrequency(String frequency)
  {
    this.entity.setFrequency(frequency);
  }
  
  @Override
  public ITargetFilters getTargetFilters()
  {
    return this.entity.getTargetFilters();
  }
  
  @JsonDeserialize(as = TargetFilters.class)
  @Override
  public void setTargetFilters(ITargetFilters targetfilters)
  {
    this.entity.setTargetFilters(targetfilters);
  }
  
  @Override
  public List<IKPITag> getKpiTags()
  {
    return this.entity.getKpiTags();
  }
  
  @JsonDeserialize(contentAs = KPITag.class)
  @Override
  public void setKpiTags(List<IKPITag> kpiTags)
  {
    this.entity.setKpiTags(kpiTags);
  }
  
  @Override
  public List<String> getRules()
  {
    return this.entity.getRules();
  }
  
  @Override
  public void setRules(List<String> rules)
  {
    this.entity.setRules(rules);
  }
  
  @Override
  public Map<String, Object> getRoles()
  {
    return this.entity.getRoles();
  }
  
  @Override
  public void setRoles(Map<String, Object> roles)
  {
    this.entity.setRoles(roles);
  }
  
  @Override
  public String getId()
  {
    return this.entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    this.entity.setId(id);
  }
  
  @Override
  public Long getVersionId()
  {
    return this.entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.entity.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return this.entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return this.entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public List<IDrillDown> getDrillDowns()
  {
    return entity.getDrillDowns();
  }
  
  @Override
  public void setDrillDowns(List<IDrillDown> drillDowns)
  {
    entity.setDrillDowns(drillDowns);
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
  public List<String> getOrganizations()
  {
    return entity.getOrganizations();
  }
  
  @Override
  public void setOrganizations(List<String> organizations)
  {
    entity.setOrganizations(organizations);
  }
  
  @Override
  public List<String> getPhysicalCatalogIds()
  {
    return entity.getPhysicalCatalogIds();
  }
  
  @Override
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds)
  {
    entity.setPhysicalCatalogIds(physicalCatalogIds);
  }
  
  @Override
  public List<String> getEndpoints()
  {
    return entity.getEndpoints();
  }
  
  @Override
  public void setEndpoints(List<String> endpoints)
  {
    entity.setEndpoints(endpoints);
  }
}
