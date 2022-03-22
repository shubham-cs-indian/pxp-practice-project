package com.cs.config.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.cs.config.idto.IConfigGoldenRecordRuleDTO;
import com.cs.config.idto.IConfigMergeEffectDTO;
import com.cs.config.idto.IConfigMergeEffectTypeDTO;
import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.ijosn.IJSONContent;

public class ConfigGoldenRecordRuleDTO extends AbstractConfigJSONDTO implements IConfigGoldenRecordRuleDTO {
  
  private static final long                       serialVersionUID  = 1L;
  /**
   * Tag keys and its corresponding PXON key map
   */
  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();
  
  /**
   * Initializing the static key map
   */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());
    
    // update only Properties
    KEY_MAP.put(ConfigTag.label, IPXON.PXONTag.label.toTag());
    KEY_MAP.put(ConfigTag.attributes, IPXON.PXONTag.attributes.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.tags, IPXON.PXONTag.tags.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.klassIds, IPXON.PXONTag.klassids.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.taxonomyIds, IPXON.PXONTag.taxonomyids.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.physicalCatalogIds, IPXON.PXONTag.physicalcatalogids.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.organizations, IPXON.PXONTag.organizations.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.mergeEffect, IPXON.PXONTag.mergeeffect.toTag());
    KEY_MAP.put(ConfigTag.isAutoCreate, IPXON.PXONTag.isautocreate.toTag());
    
    IGNORED_PXON_TAGS.add(ConfigTag.code);
  }
  
  private IConfigMergeEffectDTO mergeEffectDTO = new ConfigMergeEffectDTO();
  
  public ConfigGoldenRecordRuleDTO() throws CSFormatException
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }
  
  @Override
  public String getLabel()
  {
    return getString(ConfigTag.label);
  }
  
  @Override
  public void setLabel(String label)
  {
    setString(ConfigTag.label, label);
  }
  
  @Override
  public List<String> getAttributes()
  {
    return getJSONArray(ConfigTag.attributes);
  }
  
  @Override
  public List<String> getTags()
  {
    return getJSONArray(ConfigTag.tags);
  }
  
  @Override
  public List<String> getKlassIds()
  {
    return getJSONArray(ConfigTag.klassIds);
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    return getJSONArray(ConfigTag.taxonomyIds);
  }
  
  @Override
  public List<String> getPhysicalCatalogIds()
  {
    return getJSONArray(ConfigTag.physicalCatalogIds);
  }
  
  @Override
  public List<String> getOrganizations()
  {
    return getJSONArray(ConfigTag.organizations);
  }
  
  @Override
  public IConfigMergeEffectDTO getMergeEffect()
  {
    return mergeEffectDTO;
  }
  
  @Override
  public void setMergeEffect(IConfigMergeEffectDTO mergeEffect)
  {
    mergeEffectDTO = mergeEffect;
    configData.setField(ConfigTag.mergeEffect.name(), mergeEffect);
  }
  
  @Override
  public Boolean getIsAutoCreate()
  {
    return getBoolean(ConfigTag.isAutoCreate);
  }
  
  @Override
  public void setIsAutoCreate(Boolean isAutoCreate)
  {
    setBoolean(ConfigTag.isAutoCreate, isAutoCreate);
  }
  
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    CSEObject cse = new CSEObject(CSEObjectType.Golden_Rule);
    String code = getString(ConfigTag.code);
    cse.setCode(code);
    return cse;
  }
  
  @Override
  void fromCSExpression(JSONContentParser parser) throws CSFormatException
  {
    fromCSExpressID(parser.getCSEElement(IPXON.PXONTag.csid.toTag()));
  }
  
  @Override
  public void fromPXON(String json) throws CSFormatException
  {
    super.fromPXON(json);
    IJSONContent jsonContent = getJSONContent(ConfigTag.mergeEffect);
    mergeEffectDTO.fromPXON(jsonContent.toString());
  }
  
  @Override
  public void fromJSON(String json) throws CSFormatException
  {
    super.fromJSON(json);
    IJSONContent jsonContent = getJSONContent(ConfigTag.mergeEffect);
    mergeEffectDTO.fromJSON(jsonContent.toString());
  }
  
  @Override
  protected JSONContent toConfigPXONContent() throws CSFormatException
  {
    JSONContent pxonOutput = super.toConfigPXONContent();
    pxonOutput.setField(KEY_MAP.get(ConfigTag.mergeEffect), ((ConfigMergeEffectDTO) mergeEffectDTO).toConfigPXONContent().toJSONObject());
    
    return pxonOutput;
  }

  @Override
  public void setAttributes(List<String> attributes)
  {
    configData.setStringArrayField(ConfigTag.attributes.name(), attributes);
  }

  @Override
  public void setTags(List<String> tags)
  {
    configData.setStringArrayField(ConfigTag.tags.name(), tags);
  }

  @Override
  public void setKlassIds(List<String> klassIds)
  {
    configData.setStringArrayField(ConfigTag.klassIds.name(), klassIds);
  }

  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    configData.setStringArrayField(ConfigTag.taxonomyIds.name(), taxonomyIds);
  }

  @Override
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds)
  {
    configData.setStringArrayField(ConfigTag.physicalCatalogIds.name(), physicalCatalogIds);
  }

  @Override
  public void setOrganizations(List<String> organizations)
  {
    configData.setStringArrayField(ConfigTag.organizations.name(), organizations);
  }
  
  @Override
  public void setCode(String code) {
    setString(ConfigTag.code, code);
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException {
    configData.setField(ConfigTag.mergeEffect.name(), mergeEffectDTO);
    return super.toJSONBuffer();
  }
  
}
