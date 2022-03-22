package com.cs.config.idto;

import java.util.Collection;
import java.util.List;

/**
 * Organization DTO from the configuration realm
 *
 * @author abhishek
 */

public interface IConfigOrganizationDTO extends IConfigJSONDTO {
  
  public List<String> getPhysicalCatalogs();
  public String getCode();
  public String getType();
  
  public void setType(String type);

  public String getLabel();

  public void setLabel(String label);

  public String getIcon();

  public void setIcon(String icon);
  
  public List<String> getPortals();
  
  public List<String> getTaxonomyIds();
  
  public List<String> getKlassIds();
  
  public List<String> getEndpointIds();
  
  public List<String> getSystems();
  
  public List<IConfigRoleDTO> getRoles();

  public void setPhysicalCatalogs(Collection<String> physicalCatalogs);
  public void setPortals(Collection<String> portals);
  public void setTaxonomyIds(Collection<String> taxonomyIds);
  public void setKlassIds(Collection<String> klassIds);
  public void setEndpointIds(Collection<String> endpointIds);
  public void setSystems(Collection<String> systems);
}
