package com.cs.config.dto;

import com.cs.config.idto.IConfigTabDTO;
import com.cs.config.idto.IConfigUserDTO;
import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.UserDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigTabDTO extends AbstractConfigJSONDTO implements IConfigTabDTO {

  private static final long                       serialVersionUID  = 1L;
  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();

  /** Initializing the static key map */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.isStandard, IPXON.PXONTag.isstandard.toReadOnlyTag());

    // update only Properties
    KEY_MAP.put(ConfigTag.label, IPXON.PXONTag.label.toTag());
    KEY_MAP.put(ConfigTag.icon, IPXON.PXONTag.icon.toTag());
    KEY_MAP.put(ConfigTag.sequence, IPXON.PXONTag.sequence.toTag());
    KEY_MAP.put(ConfigTag.propertySequenceList, IPXON.PXONTag.propertySequenceList.toJSONArrayTag());
    
    IGNORED_PXON_TAGS.add(ConfigTag.code);
  }

  public ConfigTabDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }

  @Override
  public void fromJSON(String json) throws CSFormatException
  {
    super.fromJSON(json);
  }

  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    CSEObject ecse = new CSEObject(ICSEElement.CSEObjectType.Tab);
    String code = getString(ConfigTag.code);
    ecse.setCode(code);
    return ecse;
  }

  @Override
   void fromCSExpression(JSONContentParser parser) throws CSFormatException
  {
    fromCSExpressID(parser.getCSEElement(IPXON.PXONTag.csid.toTag()));
  }

  @Override
  public List<String> getPropertySequenceList()
  {
     return getJSONArray(ConfigTag.propertySequenceList);
  }

  @Override
  public void setPropertySequenceList(List<String> propertySequenceList)
  {
    JSONArray jsonArray = new JSONArray();
    jsonArray.addAll(propertySequenceList);
    setJSONArray(ConfigTag.propertySequenceList, jsonArray);
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
  public Integer getSequence()
  {
    return getInt(ConfigTag.sequence);
  }

  @Override
  public void setSequence(Integer sequence)
  {
    setInt(ConfigTag.sequence, sequence);
  }

  @Override
  public void setCode(String code)
  {
    setString(ConfigTag.code, code);
  }

}
