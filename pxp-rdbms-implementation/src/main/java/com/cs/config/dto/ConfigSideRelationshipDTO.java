package com.cs.config.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.cs.config.idto.IConfigSideRelationshipDTO;
import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * @author tauseef
 **/
public class ConfigSideRelationshipDTO extends AbstractConfigJSONDTO implements IConfigSideRelationshipDTO {
  
  /**
   * RelationshipSide keys and its corresponding PXON key map
   */
  private static final BidiMap<ConfigTag, String> KEY_MAP = new DualHashBidiMap<ConfigTag, String>();
  
  private static final Set<ConfigTag> IGNORED_PXON_TAGS = new HashSet<>();

  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.cardinality, IPXON.PXONTag.cardinality.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.classCode, IPXON.PXONTag.classcode.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());

    KEY_MAP.put(ConfigTag.label, IPXON.PXONTag.label.toTag());
    KEY_MAP.put(ConfigTag.contextCode, IPXON.PXONTag.contextcode.toTag());
    KEY_MAP.put(ConfigTag.isVisible, IPXON.PXONTag.isvisible.toTag());
    KEY_MAP.put(ConfigTag.couplings, IPXON.PXONTag.couplings.toJSONArrayTag());

    IGNORED_PXON_TAGS.add(ConfigTag.code);
  }

  /**
   * Initializing the static key map
   */
  public ConfigSideRelationshipDTO()
  {
    super(KEY_MAP,  IGNORED_PXON_TAGS);
  }

  @Override
  public ICSEElement toCSExpressID()
  {
   return null;
  }

  @Override
  void fromCSExpression (JSONContentParser parser) throws CSFormatException {
    //No CSE element
  }

  @Override
  public String getLabel()
  {
    return getString(ConfigTag.label);
  }
  
  @Override
  public String getClassCode()
  {
    return getString(ConfigTag.classCode);
  }
  
  @Override
  public String getCardinality()
  {
    return getString(ConfigTag.cardinality);
  }
  
  @Override
  public boolean isVisible()
  {
    return getBoolean(ConfigTag.isVisible);
  }
  
  @Override
  public String getContextCode()
  {
    return getString(ConfigTag.contextCode);
  }
  
  @Override
  public Collection<IJSONContent> getCouplings()
  {
    return getJSONArray(ConfigTag.couplings);
  }

  @Override
  public void setLabel(String label)
  {
     setString(ConfigTag.label, label);
  }

  @Override
  public void setClassCode(String classCode)
  {
    setString(ConfigTag.classCode, classCode);
  }

  @Override
  public void setCardinality(String cardinality)
  {
    setString(ConfigTag.cardinality, cardinality);
  }

  @Override
  public void setIsVisible(boolean isVisible)
  {
    setBoolean(ConfigTag.isVisible, isVisible);
  }

  @Override
  public void setContextCode(String contextCode)
  {
    setString(ConfigTag.contextCode, contextCode);
  }
  
  @Override
  public void setCode(String code)
  {
    setString(ConfigTag.code, code);
  }

  @Override
  public void setCouplings(Collection<IJSONContent> couplings)
  {
    configData.setField(ConfigTag.couplings.toString(), couplings);
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException {
    return super.toJSONBuffer();
  }
}
