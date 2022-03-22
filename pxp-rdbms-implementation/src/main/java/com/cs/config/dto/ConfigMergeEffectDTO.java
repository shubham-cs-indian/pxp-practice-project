package com.cs.config.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.json.simple.JSONArray;

import com.cs.config.idto.IConfigMergeEffectDTO;
import com.cs.config.idto.IConfigMergeEffectTypeDTO;
import com.cs.config.idto.IConfigRuleEntityDTO;
import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;

public class ConfigMergeEffectDTO extends AbstractConfigJSONDTO implements IConfigMergeEffectDTO {
  
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
    KEY_MAP.put(ConfigTag.attributes, IPXON.PXONTag.attributes.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.tags, IPXON.PXONTag.tags.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.relationships, IPXON.PXONTag.relationships.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.natureRelationships, IPXON.PXONTag.naturerelationships.toJSONArrayTag());
    
    IGNORED_PXON_TAGS.add(ConfigTag.code);
  }
  
  private final List<IConfigMergeEffectTypeDTO> attributes          = new ArrayList<>();
  private final List<IConfigMergeEffectTypeDTO> tags                = new ArrayList<>();
  private final List<IConfigMergeEffectTypeDTO> relationships       = new ArrayList<>();
  private final List<IConfigMergeEffectTypeDTO> natureRelationships = new ArrayList<>();
  
  public ConfigMergeEffectDTO() throws CSFormatException
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }
  
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    return null;
  }
  
  @Override
  void fromCSExpression(JSONContentParser parser) throws CSFormatException
  {
    // No CSE Element
  }
  
  @Override
  public List<IConfigMergeEffectTypeDTO> getAttributes()
  {
    return attributes;
  }
  
  @Override
  public List<IConfigMergeEffectTypeDTO> getTags()
  {
    return tags;
  }
  
  @Override
  public List<IConfigMergeEffectTypeDTO> getRelationships()
  {
    return relationships;
  }
  
  @Override
  public List<IConfigMergeEffectTypeDTO> getNatureRelationships()
  {
    return natureRelationships;
  }
  
  @Override
  public void fromPXON(String json) throws CSFormatException
  {
    super.fromPXON(json);
    attributes.clear();
    JSONArray attributeList = getJSONArray(ConfigTag.attributes);
    for (Object attribute : attributeList) {
      ConfigMergeEffectTypeDTO attributeDTO = new ConfigMergeEffectTypeDTO();
      attributeDTO.fromPXON(attribute.toString());
      attributes.add(attributeDTO);
    }
    
    tags.clear();
    JSONArray tagList = getJSONArray(ConfigTag.tags);
    for (Object tag : tagList) {
      ConfigMergeEffectTypeDTO tagDTO = new ConfigMergeEffectTypeDTO();
      tagDTO.fromPXON(tag.toString());
      tags.add(tagDTO);
    }
    
    relationships.clear();
    JSONArray relationshipList = getJSONArray(ConfigTag.relationships);
    for (Object relationship : relationshipList) {
      ConfigMergeEffectTypeDTO relationshipDTO = new ConfigMergeEffectTypeDTO();
      relationshipDTO.fromPXON(relationship.toString());
      relationships.add(relationshipDTO);
    }
    
    natureRelationships.clear();
    JSONArray natureRelationshipList = getJSONArray(ConfigTag.natureRelationships);
    for (Object natureRelationship : natureRelationshipList) {
      ConfigMergeEffectTypeDTO natureRelationshipDTO = new ConfigMergeEffectTypeDTO();
      natureRelationshipDTO.fromPXON(natureRelationship.toString());
      natureRelationships.add(natureRelationshipDTO);
    }
    
  }
  
  @Override
  public void fromJSON(String json) throws CSFormatException  
  {
    super.fromJSON(json);
    attributes.clear();
    JSONArray attributeList = getJSONArray(ConfigTag.attributes);
    for (Object attribute : attributeList) {
      ConfigMergeEffectTypeDTO attributeDTO = new ConfigMergeEffectTypeDTO();
      attributeDTO.fromJSON(attribute.toString());
      attributes.add(attributeDTO);
    }
    
    tags.clear();
    JSONArray tagList = getJSONArray(ConfigTag.tags);
    for (Object tag : tagList) {
      ConfigMergeEffectTypeDTO tagDTO = new ConfigMergeEffectTypeDTO();
      tagDTO.fromJSON(tag.toString());
      tags.add(tagDTO);
    }
    
    relationships.clear();
    JSONArray relationshipList = getJSONArray(ConfigTag.relationships);
    for (Object relationship : relationshipList) {
      ConfigMergeEffectTypeDTO relationshipDTO = new ConfigMergeEffectTypeDTO();
      relationshipDTO.fromJSON(relationship.toString());
      relationships.add(relationshipDTO);
    }
    
    natureRelationships.clear();
    JSONArray natureRelationshipList = getJSONArray(ConfigTag.natureRelationships);
    for (Object natureRelationship : natureRelationshipList) {
      ConfigMergeEffectTypeDTO natureRelationshipDTO = new ConfigMergeEffectTypeDTO();
      natureRelationshipDTO.fromJSON(natureRelationship.toString());
      natureRelationships.add(natureRelationshipDTO);
    }
  }
  
  @Override
  protected JSONContent toConfigPXONContent() throws CSFormatException
  {
    JSONContent pxonOutput = super.toConfigPXONContent();
    // attributes preparation
    JSONArray pxonAttributes = new JSONArray();
    for (IConfigMergeEffectTypeDTO attributeDTO : attributes) {
      pxonAttributes.add(((ConfigMergeEffectTypeDTO) attributeDTO).toConfigPXONContent().toJSONObject());
    }
    pxonOutput.setField(KEY_MAP.get(ConfigTag.attributes), pxonAttributes);
    
    // tags preparation
    JSONArray pxonTags = new JSONArray();
    for (IConfigMergeEffectTypeDTO tagDTO : tags) {
      pxonTags.add(((ConfigMergeEffectTypeDTO) tagDTO).toConfigPXONContent().toJSONObject());
    }
    pxonOutput.setField(KEY_MAP.get(ConfigTag.tags), pxonTags);
    
    // relationships preparation
    JSONArray pxonRelations = new JSONArray();
    for (IConfigMergeEffectTypeDTO relationDTO : relationships) {
      pxonRelations.add(((ConfigMergeEffectTypeDTO) relationDTO).toConfigPXONContent().toJSONObject());
    }
    pxonOutput.setField(KEY_MAP.get(ConfigTag.relationships), pxonRelations);
    
    // nature relationships preparation
    JSONArray pxonNatureRelations = new JSONArray();
    for (IConfigMergeEffectTypeDTO relationDTO : natureRelationships) {
      pxonNatureRelations.add(((ConfigMergeEffectTypeDTO) relationDTO).toConfigPXONContent().toJSONObject());
    }
    pxonOutput.setField(KEY_MAP.get(ConfigTag.natureRelationships), pxonNatureRelations);
    
    return pxonOutput;
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException {
    List<Object> attributeDTOs = new ArrayList<>();
    for(IConfigMergeEffectTypeDTO attribute : attributes) {
      attributeDTOs.add(attribute);
    }
    configData.setField(ConfigTag.attributes.toString(), attributeDTOs);
    
    List<Object> tagsDTOs = new ArrayList<>();
    for(IConfigMergeEffectTypeDTO tag : tags) {
      tagsDTOs.add(tag);
    }
    configData.setField(ConfigTag.tags.toString(), tagsDTOs);
    
    List<Object> relationshipDTOs = new ArrayList<>();
    for(IConfigMergeEffectTypeDTO relation : relationships) {
      relationshipDTOs.add(relation);
    }
    configData.setField(ConfigTag.relationships.toString(), relationshipDTOs);
    
    List<Object> natureRelationshipDTOs = new ArrayList<>();
    for(IConfigMergeEffectTypeDTO natureRelationship : natureRelationships) {
      natureRelationshipDTOs.add(natureRelationship);
    }
    configData.setField(ConfigTag.natureRelationships.toString(), natureRelationshipDTOs);
    
    return super.toJSONBuffer();
  }
}
