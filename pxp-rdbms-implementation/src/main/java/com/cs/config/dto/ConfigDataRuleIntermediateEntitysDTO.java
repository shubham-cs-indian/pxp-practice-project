package com.cs.config.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.json.simple.JSONArray;

import com.cs.config.idto.IConfigDataRuleIntermediateEntitysDTO;
import com.cs.config.idto.IConfigRuleEntityDTO;
import com.cs.config.standard.IConfigClass;
import com.cs.config.standard.IConfigMap;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;

public class ConfigDataRuleIntermediateEntitysDTO extends AbstractConfigJSONDTO implements IConfigDataRuleIntermediateEntitysDTO {
  
  private static final long                       serialVersionUID  = 1L;
  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();
  
  /** Initializing the static key map */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());
    
    // update only Properties
    KEY_MAP.put(ConfigTag.entityId, IPXON.PXONTag.entityid.toTag());
    KEY_MAP.put(ConfigTag.entityType, IPXON.PXONTag.entitytype.toTag());
    KEY_MAP.put(ConfigTag.entityAttributeType, IPXON.PXONTag.entityattributetype.toTag());
    KEY_MAP.put(ConfigTag.rules, IPXON.PXONTag.rules.toJSONArrayTag());
  }
  
  private final List<IConfigRuleEntityDTO> rules = new ArrayList<>();
  
  public ConfigDataRuleIntermediateEntitysDTO()
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
    
  }
  
  @Override
  public void fromJSON(String json) throws CSFormatException
  {
    super.fromJSON(json);
    rules.clear();
    String type = getString(ConfigTag.entityType);
    initEntityType(type);

    JSONArray ruleList = getJSONArray(ConfigTag.rules);
    for (Object rule : ruleList) {
      ConfigRuleEntityDTO configRuleDTO = new ConfigRuleEntityDTO();
      configRuleDTO.fromJSON(rule.toString());
      rules.add(configRuleDTO);
    }
  }

  private void initEntityType(String type)
  {
    if (!type.isEmpty() && !type.equals(SuperType.ATTRIBUTE.toString())) {
      IPropertyDTO.PropertyType propertyType = IConfigMap.getPropertyType(type);
      setString(ConfigTag.entityType, propertyType.getSuperType().toString());
      setString(ConfigTag.entityAttributeType, IConfigClass.PropertyClass.valueOfClassPath(type).name());
    }
  }
  
  @Override
  public void fromPXON(String json) throws CSFormatException
  {
    super.fromPXON(json);
    rules.clear();
    String type = getString(ConfigTag.entityType);
    initEntityType(type);
    JSONArray ruleList = getJSONArray(ConfigTag.rules);
    for (Object rule : ruleList) {
      ConfigRuleEntityDTO configRuleDTO = new ConfigRuleEntityDTO();
      configRuleDTO.fromPXON(rule.toString());
      rules.add(configRuleDTO);
    }
    
  }
  
  @Override
  protected JSONContent toConfigPXONContent() throws CSFormatException
  {
    JSONContent pxonOutput = super.toConfigPXONContent();
    // attributes preparation
    JSONArray pxonRules = new JSONArray();
    for (IConfigRuleEntityDTO ruleDTO : rules) {
      pxonRules.add(((ConfigRuleEntityDTO) ruleDTO).toConfigPXONContent().toJSONObject());
    }
    pxonOutput.setField(KEY_MAP.get(ConfigTag.rules), pxonRules);
    return pxonOutput;
  }
  
  @Override
  public String getCode()
  {
    return getString(ConfigTag.code);
  }
  
  @Override
  public void setCode(String code)
  {
    setString(ConfigTag.code, code);
  }
  
  @Override
  public String getEntityId()
  {
    return getString(ConfigTag.entityId);
  }
  
  @Override
  public void setEntityId(String entityId)
  {
    setString(ConfigTag.entityId, entityId);
  }

  @Override
  public List<IConfigRuleEntityDTO> getRules()
  {
    return rules;
  }
  
  @Override
  public String getEntityType()
  {
    return getString(ConfigTag.entityType);
  }

  @Override
  public void setEntityType(String entityType)
  {
    setString(ConfigTag.entityType, entityType);
  }

  @Override
  public String getEntityAttributeType()
  {
    return getString(ConfigTag.entityAttributeType);
  }

  @Override
  public void setEntityAttributeType(String entityAttributeType)
  {
    setString(ConfigTag.entityAttributeType, entityAttributeType);
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException {
    List<Object> ruleDTOs = new ArrayList<>();
    for(IConfigRuleEntityDTO rule : rules) {
      ruleDTOs.add(rule);
    }
    configData.setField(ConfigTag.rules.toString(), ruleDTOs);
    return super.toJSONBuffer();
  }

  
  
}
