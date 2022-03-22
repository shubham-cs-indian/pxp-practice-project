package com.cs.core.config.interactor.entity.governancerule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.List;
import java.util.Map;

public interface IKeyPerformanceIndicator extends IEntity {
  
  public static final String LABEL                = "label";
  public static final String FREQUENCY            = "frequency";
  public static final String TARGET_FILTERS       = "targetFilters";
  public static final String KPI_TAGS             = "kpiTags";
  public static final String RULES                = "rules";
  public static final String ROLES                = "roles";
  public static final String DRILL_DOWNS          = "drillDowns";
  public static final String CODE                 = "code";
  public static final String DASHBOARD_TAB_ID     = "dashboardTabId";
  public static final String PHYSICAL_CATALOG_IDS = "physicalCatalogIds";
  public static final String ORGANIZATIONS        = "organizations";
  public static final String ENDPOINTS            = "endpoints";
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getFrequency();
  
  public void setFrequency(String frequency);
  
  public ITargetFilters getTargetFilters();
  
  public void setTargetFilters(ITargetFilters targetFilters);
  
  public List<IKPITag> getKpiTags();
  
  public void setKpiTags(List<IKPITag> kpiTags);
  
  public List<String> getRules();
  
  public void setRules(List<String> rules);
  
  public Map<String, Object> getRoles();
  
  public void setRoles(Map<String, Object> roles);
  
  public List<IDrillDown> getDrillDowns();
  
  public void setDrillDowns(List<IDrillDown> drillDowns);
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getDashboardTabId();
  
  public void setDashboardTabId(String dashboardTabId);
  
  public List<String> getOrganizations();
  
  public void setOrganizations(List<String> organizations);
  
  public List<String> getPhysicalCatalogIds();
  
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds);
  
  public List<String> getEndpoints();
  
  public void setEndpoints(List<String> endpoints);
}
