package com.cs.core.config.interactor.entity.organization;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;

import java.util.List;

public interface IOrganization extends IConfigMasterEntity {
  
  public static final String PHYSICAL_CATALOGS = "physicalCatalogs";
  public static final String PORTALS           = "portals";
  public static final String ROLE_IDS          = "roleIds";
  public static final String TAXONOMY_IDS      = "taxonomyIds";
  public static final String KLASS_IDS         = "klassIds";
  public static final String ENDPOINT_IDS      = "endpointIds";
  public static final String IS_STANDARD       = "isStandard";
  public static final String SYSTEMS           = "systems";
  
  public List<String> getPhysicalCatalogs();
  
  public void setPhysicalCatalogs(List<String> physicalCatalogs);
  
  public List<String> getPortals();
  
  public void setPortals(List<String> portals);
  
  public List<String> getRoleIds();
  
  public void setRoleIds(List<String> roleIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getEndpointIds();
  
  public void setEndpointIds(List<String> endpointIds);
  
  public Boolean getIsStandard();
  
  public void setIsStandard(Boolean isStandard);
  
  public List<String> getSystems();
  
  public void setSystems(List<String> systems);
}
