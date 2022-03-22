package com.cs.config.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.cs.config.idto.IConfigDataRuleDTO;
import com.cs.config.idto.IConfigDataRuleIntermediateEntitysDTO;
import com.cs.config.idto.IConfigDataRuleTagsDTO;
import com.cs.config.idto.IConfigNormalizationDTO;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.ijosn.IJSONContent;

@SuppressWarnings("unchecked")
public class ConfigDataRuleDTO extends AbstractConfigJSONDTO implements IConfigDataRuleDTO {
  
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
    KEY_MAP.put(ConfigTag.type, IPXON.PXONTag.type.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.isLanguageDependent, IPXON.PXONTag.islanguagedependent.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.isStandard, IPXON.PXONTag.isstandard.toReadOnlyTag());
    
    // update only Properties
    KEY_MAP.put(ConfigTag.label, IPXON.PXONTag.label.toTag());
    KEY_MAP.put(ConfigTag.attributes, IPXON.PXONTag.attributes.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.types, IPXON.PXONTag.types.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.tags, IPXON.PXONTag.tags.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.ruleViolations, IPXON.PXONTag.ruleviolations.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.normalizations, IPXON.PXONTag.normalizations.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.taxonomies, IPXON.PXONTag.taxonomies.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.physicalCatalogIds, IPXON.PXONTag.physicalcatalogids.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.organizations, IPXON.PXONTag.organizations.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.languages, IPXON.PXONTag.languages.toJSONArrayTag());
    
    IGNORED_PXON_TAGS.add(ConfigTag.code);
  }
  
  private final List<IConfigDataRuleIntermediateEntitysDTO> attributes     = new ArrayList<>();
  private final List<IConfigDataRuleTagsDTO>                tags           = new ArrayList<>();
  private final List<IConfigNormalizationDTO>               normalizations = new ArrayList<>();
  
  public ConfigDataRuleDTO() throws CSFormatException
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }
  
  @Override
  public void fromPXON(String json) throws CSFormatException
  {
    super.fromPXON(json);
    attributes.clear();
    JSONArray attributeList = getJSONArray(ConfigTag.attributes);
    for (Object attribute : attributeList) {
      ConfigDataRuleIntermediateEntitysDTO attributeDTO = new ConfigDataRuleIntermediateEntitysDTO();
      attributeDTO.fromPXON(attribute.toString());
      attributes.add(attributeDTO);
    }
    
    tags.clear();
    JSONArray tagList = getJSONArray(ConfigTag.tags);
    for (Object tag : tagList) {
      ConfigDataRuleTagsDTO tagDTO = new ConfigDataRuleTagsDTO();
      tagDTO.fromPXON(tag.toString());
      tags.add(tagDTO);
    }
    
    normalizations.clear();
    JSONArray normalizationList = getJSONArray(ConfigTag.normalizations);
    for (Object normalization : normalizationList) {
      ConfigNormalizationDTO normalizationDTO = new ConfigNormalizationDTO();
      normalizationDTO.fromPXON(normalization.toString());
      normalizations.add(normalizationDTO);
    }
    
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
  public void fromJSON(String json) throws CSFormatException
  {
    super.fromJSON(json);
    attributes.clear();
    JSONArray attributeList = getJSONArray(ConfigTag.attributes);
    for (Object attribute : attributeList) {
      ConfigDataRuleIntermediateEntitysDTO attributeDTO = new ConfigDataRuleIntermediateEntitysDTO();
      attributeDTO.fromJSON(attribute.toString());
      attributes.add(attributeDTO);
    }
    
    tags.clear();
    JSONArray tagList = getJSONArray(ConfigTag.tags);
    for (Object tag : tagList) {
      ConfigDataRuleTagsDTO tagDTO = new ConfigDataRuleTagsDTO();
      tagDTO.fromJSON(tag.toString());
      tags.add(tagDTO);
    }
    
    normalizations.clear();
    JSONArray normalizationList = getJSONArray(ConfigTag.normalizations);
    for (Object normalization : normalizationList) {
      ConfigNormalizationDTO normalizationDTO = new ConfigNormalizationDTO();
      normalizationDTO.fromJSON(normalization.toString());
      normalizations.add(normalizationDTO);
    }
  }
  
  
  @Override
  protected JSONContent toConfigPXONContent() throws CSFormatException {
    JSONContent pxonOutput = super.toConfigPXONContent();
    // attributes preparation
    JSONArray pxonAttributes = new JSONArray();
    for (IConfigDataRuleIntermediateEntitysDTO attributeDTO : attributes) {
      pxonAttributes.add(((ConfigDataRuleIntermediateEntitysDTO) attributeDTO).toConfigPXONContent().toJSONObject());
    }
    pxonOutput.setField(KEY_MAP.get(ConfigTag.attributes), pxonAttributes);
    
    // tags preparation
    JSONArray pxonTags = new JSONArray();
    for (IConfigDataRuleTagsDTO tagDTO : tags) {
      pxonTags.add(((ConfigDataRuleTagsDTO) tagDTO).toConfigPXONContent().toJSONObject());
    }
    pxonOutput.setField(KEY_MAP.get(ConfigTag.tags), pxonTags);
    
    // normalizations preparation
    JSONArray pxonNormalizations = new JSONArray();
    for (IConfigNormalizationDTO normalizationDTO : normalizations) {
      pxonNormalizations.add(((ConfigNormalizationDTO) normalizationDTO).toConfigPXONContent().toJSONObject());
    }
    pxonOutput.setField(KEY_MAP.get(ConfigTag.normalizations), pxonNormalizations);
    
    return pxonOutput;
  }

  
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    CSEObject cse = new CSEObject(CSEObjectType.Rule);
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
  public List<IConfigDataRuleIntermediateEntitysDTO> getAttributes()
  {
    return attributes;
  }
  
  @Override
  public List<String> getTypes()
  {
    return getJSONArray(ConfigTag.types);
  }
  
  @Override
  public List<IConfigDataRuleTagsDTO> getTags()
  {
    return tags;
  }
  
  @Override
  public List<IJSONContent> getRuleViolations()
  {
    return getJSONArray(ConfigTag.ruleViolations);
  }
  
  @Override
  public List<IConfigNormalizationDTO> getNormalizations()
  {
    return normalizations;
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return getBoolean(ConfigTag.isStandard);
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    setBoolean(ConfigTag.isStandard, isStandard);
  }
  
  @Override
  public List<String> getTaxonomies()
  {
    return getJSONArray(ConfigTag.taxonomies);
  }
  
  @Override
  public String getType()
  {
    return getString(ConfigTag.type);
  }
  
  @Override
  public void setType(String type)
  {
    setString(ConfigTag.type, type);
  }
  
  @Override
  public List<String> getOrganizations()
  {
    return getJSONArray(ConfigTag.organizations);
  }
  
  @Override
  public List<String> getPhysicalCatalogIds()
  {
    return getJSONArray(ConfigTag.physicalCatalogIds);
  }
  
  @Override
  public Boolean getIsLanguageDependent()
  {
    return getBoolean(ConfigTag.isLanguageDependent);
  }
  
  @Override
  public void setIsLanguageDependent(Boolean isLanguageDependent)
  {
    setBoolean(ConfigTag.isLanguageDependent, isLanguageDependent);
  }
  
  @Override
  public List<String> getLanguages()
  {
    return getJSONArray(ConfigTag.languages);
  }

  @Override
  public void setTypes(List<String> types)
  {
    configData.setField(ConfigTag.types.toString(), types);
  }

  @Override
  public void setOrganizations(List<String> organizations)
  {
    configData.setField(ConfigTag.organizations.toString(), organizations);
  }

  @Override
  public void setRuleVioloation(List<IJSONContent> ruleViolations)
  {
    configData.setField(ConfigTag.ruleViolations.toString(), ruleViolations);
  }

  @Override
  public void setTaxonomyCodes(List<String> taxonomyCodes)
  {
    configData.setField(ConfigTag.taxonomies.toString(), taxonomyCodes);
  }

  @Override
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds)
  {
    configData.setField(ConfigTag.physicalCatalogIds.toString(), physicalCatalogIds);
  }

  @Override
  public void setLanguages(List<String> languagesCodes)
  {
    configData.setField(ConfigTag.languages.toString(), languagesCodes);
  }
  
  @Override
  public void setCode(String code) {
    setString(ConfigTag.code, code);
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException {
    List<Object> attributesDTOs = new ArrayList<>();
    for(IConfigDataRuleIntermediateEntitysDTO attribute : attributes) {
      attributesDTOs.add(attribute);
    }
    configData.setField(ConfigTag.attributes.toString(), attributesDTOs);
    
    List<Object> tagDTOs = new ArrayList<>();
    for(IConfigDataRuleTagsDTO tag : tags) {
      tagDTOs.add(tag);
    }
    configData.setField(ConfigTag.tags.toString(), tagDTOs);
    
    List<Object> normalizationDTOs = new ArrayList<>();
    for(IConfigNormalizationDTO normalization : normalizations) {
      normalizationDTOs.add(normalization);
    }
    configData.setField(ConfigTag.normalizations.toString(), normalizationDTOs);
    return super.toJSONBuffer();
  }
  
  @Override
  public String getCode() {
    return getString(ConfigTag.code);
  }
  
}
