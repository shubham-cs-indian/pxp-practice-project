package com.cs.config.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.json.simple.JSONArray;

import com.cs.config.idto.IConfigDataRuleTagRuleDTO;
import com.cs.config.idto.IConfigRuleEntityDTO;
import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.ijosn.IJSONContent;

public class ConfigDataRuleTagRuleDTO extends AbstractConfigJSONDTO implements IConfigDataRuleTagRuleDTO {
  
  private static final long serialVersionUID = 1L;
  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();
  
  /** Definition of the tag and its PXON key map */
  static {
    // Readonly Properties
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.type, IPXON.PXONTag.type.toReadOnlyTag());
    
    // Updatable Properties
    KEY_MAP.put(ConfigTag.color, IPXON.PXONTag.color.toTag());
    KEY_MAP.put(ConfigTag.description, IPXON.PXONTag.description.toTag());
    KEY_MAP.put(ConfigTag.tagValues, IPXON.PXONTag.tagvalues.toJSONArrayTag());
    
  }
  
  public ConfigDataRuleTagRuleDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }
  
  @Override
  public ICSEElement toCSExpressID()
  {
    return null;
  }
  
  @Override
  void fromCSExpression(JSONContentParser parser) throws CSFormatException
  {
    // No CSE Element
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
  public String getColor()
  {
    return getString(ConfigTag.color);
  }

  @Override
  public void setColor(String color)
  {
    setString(ConfigTag.color, color);
  }

  @Override
  public String getDescription()
  {
    return getString(ConfigTag.description);
  }

  @Override
  public void setDescription(String description)
  {
    setString(ConfigTag.description, description);
  }

  @Override
  public List<IJSONContent> getTagValues()
  {
    return getJSONArray(ConfigTag.tagValues);
  }

  @Override
  public void setTagValues(List<IJSONContent> tagValues)
  {
    JSONArray jsonArray = new JSONArray();
    jsonArray.addAll(tagValues);
    setJSONArray(ConfigTag.tagValues, jsonArray);
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException {
    return super.toJSONBuffer();
  }

}
