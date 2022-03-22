package com.cs.config.idto;

import java.util.List;
import java.util.Map;

/**
 * Role DTO from the configuration realm
 *
 * @author abhishek
 */

public interface IConfigRoleDTO extends IConfigJSONDTO {

  public String getCode();

  public void setCode(String code);

  public String getLabel();

  public void setLabel(String label);

  public List<String> getEndpoints();

  public List<String> getKpis();

  public List<String> getUsers();

  public List<String> getEntities();

  public List<String> getPhysicalCatalogs();

  public List<String> getPortals();

  public List<String> getTargetKlasses();

  public List<String> getTargetTaxonomies();

  public String getRoleType();

  public void setRoleType(String roleType);

  public List<String> getSystems();

  public Boolean isDashboardEnable();

  public void setIsDashboardEnable(Boolean isDashboardEnable);

  public String getType();

  public void setType(String type);

  public Boolean getIsBackgroundRole();

  public void setIsBackgroundRole(Boolean isDashboardEnable);

  public Boolean getIsStandard();

  public void setIsStandard(Boolean isStandard);

  public void setPhysicalCatalogs(List<String> physicalCatalogs);

  public void setPortals(List<String> portals);

  public void setSystems(List<String> systems);

  public void setEndpoints(List<String> endpoints);

  public void setKpis(List<String> kpis);

  public void setEntities(List<String> entities);

  public void setTargetKlasses(List<String> targetKlasses);

  public void setTargetTaxonomies(List<String> targetTaxonomies);

  public void setUsers(List<String> users);
  
  public Boolean isReadOnly();
  
  public void setIsReadOnly(Boolean isReadOnly);
}
