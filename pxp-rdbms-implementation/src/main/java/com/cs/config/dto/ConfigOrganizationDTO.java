package com.cs.config.dto;

import java.util.*;

import com.cs.core.json.JSONBuilder;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.json.simple.JSONArray;

import com.cs.config.idto.IConfigOrganizationDTO;
import com.cs.config.idto.IConfigRoleDTO;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import org.json.simple.JSONObject;

public class ConfigOrganizationDTO extends AbstractConfigJSONDTO implements IConfigOrganizationDTO{
  
  private static final long                       serialVersionUID  = 1L;
  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();
  
  /** Initializing the static key map */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.type, IPXON.PXONTag.type.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.statusTag, IPXON.PXONTag.statustag.toReadOnlyTag());
    
    // update only Properties
    KEY_MAP.put(ConfigTag.label, IPXON.PXONTag.label.toTag());
    KEY_MAP.put(ConfigTag.icon, IPXON.PXONTag.icon.toTag());
    KEY_MAP.put(ConfigTag.physicalCatalogs, IPXON.PXONTag.physicalCatalogs.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.portals, IPXON.PXONTag.portals.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.taxonomyIds, IPXON.PXONTag.taxonomyids.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.klassIds, IPXON.PXONTag.klassids.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.endpointIds, IPXON.PXONTag.endpointIds.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.systems, IPXON.PXONTag.systems.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.roles, IPXON.PXONTag.roles.toJSONArrayTag());
    
    IGNORED_PXON_TAGS.add(ConfigTag.code);
  }
  
  private final List<IConfigRoleDTO> roles = new ArrayList<>();
  
  public ConfigOrganizationDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }
  
  @Override
  public void fromPXON(String json) throws CSFormatException
  {
    super.fromPXON(json);
    roles.clear();
    JSONArray roleList = getJSONArray(ConfigTag.roles);
    configData.deleteField(ConfigTag.roles.name());
    for (Object role : roleList) {
      ConfigRoleDTO roleDTO = new ConfigRoleDTO();
      roleDTO.fromPXON(role.toString());
      roles.add(roleDTO);
    }

    JSONObject pxonInput = JSONContent.StringToJSON(json);
    JSONContentParser parser = new JSONContentParser(pxonInput);
    String type = parser.getString(IPXON.PXONTag.type.toReadOnlyTag());
    configData.setField(ConfigTag.type.name(), type);
  }

  @Override
  public String getLabel()
  {
    return getString(ConfigTag.label);
  }

  @Override
  public String getIcon()
  {
    return getString(ConfigTag.icon);
  }

  @Override
  public String getType()
  {
    return getString(ConfigTag.type);
  }
  
  @Override
  public void fromJSON(String json) throws CSFormatException
  {
    super.fromJSON(json);
    
    roles.clear();
    JSONArray roleList = getJSONArray(ConfigTag.roles);
    for (Object role : roleList) {
      ConfigRoleDTO roleDTO = new ConfigRoleDTO();
      roleDTO.fromJSON(role.toString());
      roles.add(roleDTO);
    }
  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(), JSONBuilder.newJSONArray(ConfigTag.roles.name(), roles));
  }

  @Override
  protected JSONContent toConfigPXONContent() throws CSFormatException
  {
    JSONContent pxonOutput = super.toConfigPXONContent();
    
    // roles preparation
    JSONArray pxonRoles = new JSONArray();
    for (IConfigRoleDTO roleDTO : roles) {
      pxonRoles.add(((ConfigRoleDTO) roleDTO).toConfigPXONContent().toJSONObject());
    }
    pxonOutput.setField(KEY_MAP.get(ConfigTag.roles), pxonRoles);

    return pxonOutput;
  }

  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    CSEObject cse = new CSEObject(CSEObjectType.Organization);
    String code = getString(ConfigTag.code);
    cse.setCode(code);
    return cse;
  }

  @Override
  void fromCSExpression (JSONContentParser parser) throws CSFormatException
  {
    fromCSExpressID( parser.getCSEElement(IPXON.PXONTag.csid.toTag()));
  }
  
  @Override
  public void setType(String type)
  {
	setString(ConfigTag.type, type);
  }

  @Override
  public void setLabel(String label)
  {
    setString(ConfigTag.label, label);
  }

  @Override
  public void setIcon(String icon)
  {
    setString(ConfigTag.icon, icon);
  }

  @Override
  public void setCode(String code) 
  {
    setString(ConfigTag.code, code);
  }

  @Override
  public void setPhysicalCatalogs(Collection<String> physicalCatalogs)
  {
    configData.setStringArrayField(ConfigTag.physicalCatalogs.toString(), physicalCatalogs);
  }

  @Override
  public void setPortals(Collection<String> portals)
  {
    configData.setStringArrayField(ConfigTag.portals.toString(), portals);
  }

  @Override
  public void setTaxonomyIds(Collection<String> taxonomyIds)
  {
    configData.setStringArrayField(ConfigTag.taxonomyIds.toString(), taxonomyIds);
  }

  @Override
  public void setKlassIds(Collection<String> klassIds)
  {
    configData.setStringArrayField(ConfigTag.klassIds.toString(), klassIds);
  }

  @Override
  public void setEndpointIds(Collection<String> endpointIds)
  {
    configData.setStringArrayField(ConfigTag.endpointIds.toString(), endpointIds);
  }

  @Override
  public void setSystems(Collection<String> systems)
  {
    configData.setStringArrayField(ConfigTag.systems.toString(), systems);
  }

  @Override
  public List<String> getPhysicalCatalogs()
  {
    return getJSONArray(ConfigTag.physicalCatalogs);
  }

  @Override
  public List<String> getPortals()
  {
    return getJSONArray(ConfigTag.portals);
  }

  @Override
  public List<String> getTaxonomyIds()
  {
    return getJSONArray(ConfigTag.taxonomyIds);
  }

  @Override
  public List<String> getKlassIds()
  {
    return getJSONArray(ConfigTag.klassIds);
  }

  @Override
  public List<String> getEndpointIds()
  {
    return getJSONArray(ConfigTag.endpointIds);
  }

  @Override
  public List<String> getSystems()
  {
    return getJSONArray(ConfigTag.systems);
  }

  @Override
  public List<IConfigRoleDTO> getRoles()
  {
    return roles;
  }

}
