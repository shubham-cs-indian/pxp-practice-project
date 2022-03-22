package com.cs.config.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.cs.config.idto.IConfigRuleEntityDTO;
import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.standard.IConfigClass;
import com.cs.config.standard.IConfigMap;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;

public class ConfigRuleEntityDTO extends AbstractConfigJSONDTO implements IConfigRuleEntityDTO {
  
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
    
    // Updatable properties
    KEY_MAP.put(ConfigTag.type, IPXON.PXONTag.type.toTag());
    KEY_MAP.put(ConfigTag.to, IPXON.PXONTag.to.toTag());
    KEY_MAP.put(ConfigTag.from, IPXON.PXONTag.from.toTag());
    KEY_MAP.put(ConfigTag.values, IPXON.PXONTag.values.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.ruleListLinkId, IPXON.PXONTag.rulelistlinkid.toTag());
    KEY_MAP.put(ConfigTag.attributeLinkId, IPXON.PXONTag.attributelinkid.toTag());
    KEY_MAP.put(ConfigTag.entityType, IPXON.PXONTag.entitytype.toTag());
    KEY_MAP.put(ConfigTag.entityAttributeType, IPXON.PXONTag.entityattributetype.toTag());
    KEY_MAP.put(ConfigTag.klassLinkIds, IPXON.PXONTag.klasslinkids.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.shouldCompareWithSystemDate, IPXON.PXONTag.comparewithsysdate.toTag());
  }
  
  public ConfigRuleEntityDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }
  
  @Override
  public void fromJSON(String json) throws CSFormatException
  {
    super.fromJSON(json);
    initEntityType();
  }

  private void initEntityType()
  {
    // incase of attribute linkid is null
    String type = getString(ConfigTag.entityType);
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
    initEntityType();
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
  public String getTo()
  {
    return getString(ConfigTag.to);
  }
  
  @Override
  public void setTo(String to)
  {
    setString(ConfigTag.to, to);
  }
  
  @Override
  public String getFrom()
  {
    return getString(ConfigTag.from);
  }
  
  @Override
  public void setFrom(String from)
  {
    setString(ConfigTag.from, from);
  }
  
  @Override
  public List<String> getValues()
  {
    return getJSONArray(ConfigTag.values);
  }
  
  @Override
  public void setValues(List<String> values) 
  {
    configData.setField(ConfigTag.values.toString(), values);
  }
  
  @Override
  public String getRuleListLinkId()
  {
    return getString(ConfigTag.ruleListLinkId);
  }
  
  @Override
  public void setRuleListLinkId(String ruleListLinkId)
  {
    setString(ConfigTag.ruleListLinkId, ruleListLinkId);
  }
  
  @Override
  public String getAttributeLinkId()
  {
    return getString(ConfigTag.attributeLinkId);
  }
  
  @Override
  public void setAttributeLinkId(String attributeLinkId)
  {
   setString(ConfigTag.attributeLinkId, attributeLinkId); 
  }
  
  @Override
  public List<String> getKlassLinkIds()
  {
    return getJSONArray(ConfigTag.klassLinkIds);
  }
  
  @Override
  public Boolean getShouldCompareWithSystemDate()
  {
    return getBoolean(ConfigTag.shouldCompareWithSystemDate);
  }
  
  @Override
  public void setShouldCompareWithSystemDate(Boolean shouldCompareWithSystemDate)
  {
    setBoolean(ConfigTag.shouldCompareWithSystemDate, shouldCompareWithSystemDate);
  }

  @Override
  public void setKlassLinkIds(List<String> klassLinkdIds)
  {
    configData.setStringArrayField(ConfigTag.klassLinkIds.name(), klassLinkdIds);
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
  
  
  
}
