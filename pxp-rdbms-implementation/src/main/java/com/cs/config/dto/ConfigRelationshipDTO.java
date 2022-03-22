package com.cs.config.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.json.simple.JSONObject;

import com.cs.config.idto.IConfigRelationshipDTO;
import com.cs.config.idto.IConfigSideRelationshipDTO;
import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.standard.IConfigClass;
import com.cs.config.standard.IConfigMap;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.technical.exception.CSFormatException;

/**
 * @author tauseef
 **/
public class ConfigRelationshipDTO extends AbstractConfigPropertyDTO implements IConfigRelationshipDTO {
  
  /**
   * Relationship keys and its corresponding PXON key map
   */
  protected static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  protected static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();
  
  /** Definition of the relationship and its PXON key map */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.isNature, IPXON.PXONTag.isnature.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.isStandard, IPXON.PXONTag.isstandard.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.propertyIID, IPXON.PXONTag.propertyiid.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.type, IPXON.PXONTag.type.toReadOnlyTag());

    KEY_MAP.put(ConfigTag.label, IPXON.PXONTag.label.toTag());
    KEY_MAP.put(ConfigTag.relationshipType, IPXON.PXONTag.relationshiptype.toTag());
    KEY_MAP.put(ConfigTag.side1, IPXON.PXONTag.side1.toJSONContentTag());
    KEY_MAP.put(ConfigTag.side2, IPXON.PXONTag.side2.toJSONContentTag());
    KEY_MAP.put(ConfigTag.tab, IPXON.PXONTag.tab.toTag());
    KEY_MAP.put(ConfigTag.icon, IPXON.PXONTag.icon.toTag());
    KEY_MAP.put(ConfigTag.enableAfterSave, IPXON.PXONTag.enableaftersave.toTag());
    KEY_MAP.put(ConfigTag.isLite, IPXON.PXONTag.isLite.toTag());
    IGNORED_PXON_TAGS.add(ConfigTag.code);
    IGNORED_PXON_TAGS.add(ConfigTag.propertyIID);
    IGNORED_PXON_TAGS.add(ConfigTag.type);
    IGNORED_PXON_TAGS.add(ConfigTag.side1);
    IGNORED_PXON_TAGS.add(ConfigTag.side2);
  }

  private final ConfigSideRelationshipDTO side1DTO = new ConfigSideRelationshipDTO();
  private final ConfigSideRelationshipDTO side2DTO = new ConfigSideRelationshipDTO();

  /**
   * Initializing the static key map
   */
  public ConfigRelationshipDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }

  protected void initPropertyDTO() throws CSFormatException {
    propertyDTO.setCode(getString(ConfigTag.code));
    propertyDTO.setIID(getLong(ConfigTag.propertyIID));
    propertyDTO.setPropertyType(IConfigMap.getPropertyType(getString(ConfigTag.type)));
    JSONObject side1 = (JSONObject) configData.toJSONObject().get(ConfigTag.side1.toString());
    JSONObject side2 = (JSONObject) configData.toJSONObject().get(ConfigTag.side2.toString());
    side1DTO.fromJSON(side1.toString());
    side2DTO.fromJSON(side2.toString());
  }

  @Override
  public void fromPXON(String json) throws CSFormatException {
    super.fromPXON(json);
    propertyDTO.fromPXON(json);
    setLong(ConfigTag.propertyIID, propertyDTO.getPropertyIID());
    setString(ConfigTag.code, propertyDTO.getCode());
    setString(ConfigTag.type, IConfigClass.PropertyClass.RELATIONSHIP_TYPE.toString());

    // pxon side structure is loaded in configData
    // -> needs to be converted into ODB format for consistency
    JSONObject side1 = (JSONObject) configData.toJSONObject().get(ConfigTag.side1.toString());
    JSONObject side2 = (JSONObject) configData.toJSONObject().get(ConfigTag.side2.toString());
    side1DTO.fromPXON(side1.toString());
    side2DTO.fromPXON(side2.toString());

    configData.setField(ConfigTag.side1.toString(),
        (new JSONContent(side1DTO.toJSON())).toJSONObject());
    configData.setField(ConfigTag.side2.toString(),
        (new JSONContent(side2DTO.toJSON())).toJSONObject());
  }

  @Override
  protected JSONContent toConfigPXONContent() throws CSFormatException {
    JSONContent pxonOutput = super.toConfigPXONContent();
    pxonOutput.setField(KEY_MAP.get(ConfigTag.side1), side1DTO.toConfigPXONContent().toJSONObject());
    pxonOutput.setField(KEY_MAP.get(ConfigTag.side2), side2DTO.toConfigPXONContent().toJSONObject());
    return pxonOutput;
  }

  @Override
  public String getLabel()
  {
    return getString(ConfigTag.label);
  }
  
  @Override
  public boolean isNature()
  {
    return getBoolean(ConfigTag.isNature);
  }
  
  @Override
  public boolean isStandard()
  {
    return getBoolean(ConfigTag.isStandard);
  }
  
  @Override
  public IConfigSideRelationshipDTO getSide1() {
    return side1DTO;
  }
  
  @Override
  public IConfigSideRelationshipDTO getSide2() {
    return side2DTO;
  }
  
  @Override
  public String getRelationshipType()
  {
    return getString(ConfigTag.relationshipType);
  }
  
  @Override
  public String getTab()
  {
    return getString(ConfigTag.tab);
  }

  @Override
  public void setPropertyIID (long iid) {
    setLong(ConfigTag.propertyIID, iid);
    propertyDTO.setIID(iid);
  }

  @Override
  public void setLabel(String label)
  {
    setString(ConfigTag.label, label);
  }

  @Override
  public void setIsNature(boolean isNature)
  {
    setBoolean(ConfigTag.isNature, isNature);
  }

  @Override
  public void setIsStandard(boolean isStandard)
  {
    setBoolean(ConfigTag.isStandard, isStandard);
  }

  @Override
  public void setRelationshipType(String relationshipType)
  {
    setString(ConfigTag.relationshipType, relationshipType);
  }

  @Override
  public void setTab(String tab)
  {
    setString(ConfigTag.tab, tab);
  }

  @Override
  public void setPropertyDTO(String code, PropertyType type)
  {
    propertyDTO.setCode(code);
    propertyDTO.setPropertyType(type);
  }

  @Override
  public IPropertyDTO getPropertyDTO()
  {
    return propertyDTO;
  }
  
  @Override
  public String getIcon()
  {
    return getString(ConfigTag.icon);
  }

  @Override
  public void setIcon(String icon)
  {
    setString(ConfigTag.icon, icon);
  }

  @Override
  public void isEnableAfterSave(boolean enableAfterSave)
  {
    setBoolean(ConfigTag.enableAfterSave, enableAfterSave);
  }

  @Override
  public boolean isEnableAfterSave()
  {
    return getBoolean(ConfigTag.enableAfterSave);
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException {
    configData.setField(ConfigTag.side1.toString(), side1DTO);
    configData.setField(ConfigTag.side2.toString(), side2DTO);
    return super.toJSONBuffer();
  }
  
  @Override
  public boolean isLite()
  {
    return getBoolean(ConfigTag.isLite);
  }
  
  
  @Override
  public void setIsLite(boolean isLite)
  {
    setBoolean(ConfigTag.isLite, isLite);
  }
}
