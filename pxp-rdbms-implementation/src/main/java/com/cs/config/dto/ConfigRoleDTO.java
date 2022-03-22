package com.cs.config.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cs.core.json.JSONContent;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.cs.config.idto.IConfigRoleDTO;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class
ConfigRoleDTO extends AbstractConfigJSONDTO implements IConfigRoleDTO{
  
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
    KEY_MAP.put(ConfigTag.physicalCatalogs, IPXON.PXONTag.physicalCatalogs.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.portals, IPXON.PXONTag.portals.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.endpoints, IPXON.PXONTag.endpoints.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.kpis, IPXON.PXONTag.kpis.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.entities, IPXON.PXONTag.entities.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.systems, IPXON.PXONTag.systems.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.targetKlasses, IPXON.PXONTag.targetKlasses.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.targetTaxonomies, IPXON.PXONTag.targetTaxonomies.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.roleType, IPXON.PXONTag.roleType.toTag());
    KEY_MAP.put(ConfigTag.isDashboardEnable, IPXON.PXONTag.isDashboardEnable.toTag());
    KEY_MAP.put(ConfigTag.isReadOnly, IPXON.PXONTag.isReadOnly.toTag());
    KEY_MAP.put(ConfigTag.users, IPXON.PXONTag.users.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.isStandard, IPXON.PXONTag.isstandard.toTag());

    IGNORED_PXON_TAGS.add(ConfigTag.code);
  }
  
  public ConfigRoleDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }

  public void fromPXON(String json) throws CSFormatException
  {
    super.fromPXON(json);
    JSONObject pxonInput = JSONContent.StringToJSON(json);
    JSONContentParser parser = new JSONContentParser(pxonInput);
    String type = parser.getString(IPXON.PXONTag.type.toReadOnlyTag());
    configData.setField(ConfigTag.type.name(), type);

    ICSEElement cse = parser.getCSEElement(IPXON.PXONTag.csid.toTag());
    CSEObject cseObject = (CSEObject) cse;
    String code = cseObject.getCode();
    configData.setField(ConfigTag.code.name(), code);
  }
  @Override
  public String getLabel()
  {
    return getString(ConfigTag.label);
  }

  @Override
  public String getType()
  {
    return getString(ConfigTag.type);
  }
  
  @Override
  public void fromJSON(String json) throws CSFormatException {
    super.fromJSON(json);
  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException {

    return super.toJSONBuffer();
  }

  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    CSEObject cse = new CSEObject(CSEObjectType.Role);
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
  public void setCode(String code) 
  {
    setString(ConfigTag.code, code);
  }

  @Override
  public void setIsDashboardEnable(Boolean isDashboardEnable)
  {
    setBoolean(ConfigTag.isDashboardEnable, isDashboardEnable);
  }

  @Override
  public void setRoleType(String roleType)
  {
    setString(ConfigTag.roleType, roleType);
  }

  @Override
  public void setIsStandard(Boolean isStandard)
  {
    setBoolean(ConfigTag.isStandard, isStandard);
  }

  @Override
  public void setIsBackgroundRole(Boolean isBackgroundRole)
  {
    setBoolean(ConfigTag.isBackgroundRole, isBackgroundRole);
  }

  @Override
  public void setPhysicalCatalogs(List<String> physicalCatalogs)
  {
    JSONArray jsonArray = new JSONArray();
    jsonArray.addAll(physicalCatalogs);
    setJSONArray(ConfigTag.physicalCatalogs, jsonArray);
  }

  @Override
  public void setPortals(List<String> portals)
  {
    JSONArray jsonArray = new JSONArray();
    jsonArray.addAll(portals);
    setJSONArray(ConfigTag.portals, jsonArray);
  }

  @Override
  public void setSystems(List<String> systems)
  {
    JSONArray jsonArray = new JSONArray();
    jsonArray.addAll(systems);
    setJSONArray(ConfigTag.systems, jsonArray);
  }

  @Override
  public void setEndpoints(List<String> endpoints)
  {
    JSONArray jsonArray = new JSONArray();
    jsonArray.addAll(endpoints);
    setJSONArray(ConfigTag.endpoints, jsonArray);
  }

  @Override
  public void setKpis(List<String> kpis)
  {
    JSONArray jsonArray = new JSONArray();
    jsonArray.addAll(kpis);
    setJSONArray(ConfigTag.kpis, jsonArray);
  }

  @Override
  public void setEntities(List<String> entities)
  {
    JSONArray jsonArray = new JSONArray();
    jsonArray.addAll(entities);
    setJSONArray(ConfigTag.entities, jsonArray);
  }

  @Override
  public void setTargetKlasses(List<String> targetKlasses)
  {
    JSONArray jsonArray = new JSONArray();
    jsonArray.addAll(targetKlasses);
    setJSONArray(ConfigTag.targetKlasses, jsonArray);
  }

  @Override
  public void setTargetTaxonomies(List<String> targetTaxonomies)
  {
    JSONArray jsonArray = new JSONArray();
    jsonArray.addAll(targetTaxonomies);
    setJSONArray(ConfigTag.targetTaxonomies, jsonArray);
  }

  @Override
  public void setUsers(List<String> users)
  {
    JSONArray jsonArray = new JSONArray();
    jsonArray.addAll(users);
    setJSONArray(ConfigTag.users, jsonArray);
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
  public List<String> getSystems()
  {
    return getJSONArray(ConfigTag.systems);
  }

  @Override
  public List<String> getEndpoints()
  {
    return getJSONArray(ConfigTag.endpoints);
  }

  @Override
  public List<String> getKpis()
  {
    return getJSONArray(ConfigTag.kpis);
  }

  @Override
  public List<String> getEntities()
  {
    return getJSONArray(ConfigTag.entities);
  }

  @Override
  public List<String> getTargetKlasses()
  {
    return getJSONArray(ConfigTag.targetKlasses);
  }

  @Override
  public List<String> getTargetTaxonomies()
  {
    return getJSONArray(ConfigTag.targetTaxonomies);
  }

  @Override
  public List<String> getUsers()
  {
    return getJSONArray(ConfigTag.users);
  }

  @Override
  public String getRoleType()
  {
    return getString(ConfigTag.roleType);
  }

  @Override
  public Boolean isDashboardEnable()
  {
    return getBoolean(ConfigTag.isDashboardEnable);
  }

  @Override
  public Boolean getIsBackgroundRole()
  {
    return getBoolean(ConfigTag.isBackgroundRole);
  }

  @Override
  public Boolean getIsStandard()
  {
    return getBoolean(ConfigTag.isStandard);
  }
  @Override
  public Boolean isReadOnly()
  {
    return getBoolean(ConfigTag.isReadOnly);
  }

  @Override
  public void setIsReadOnly(Boolean isReadOnly)
  {
    setBoolean(ConfigTag.isReadOnly, isReadOnly);
    
  }
}
