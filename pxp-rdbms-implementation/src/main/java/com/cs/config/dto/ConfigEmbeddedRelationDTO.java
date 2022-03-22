package com.cs.config.dto;

import com.cs.config.idto.IConfigEmbeddedRelationDTO;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.ijosn.IJSONContent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

/**
 * @author tauseef
 */
public class ConfigEmbeddedRelationDTO extends AbstractConfigJSONDTO implements IConfigEmbeddedRelationDTO {
  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();
  
  /** Definition of the tag and its PXON key map */
  static {
    // Readonly Properties
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.type, IPXON.PXONTag.type.toReadOnlyTag());

    // Updatable Properties
    KEY_MAP.put(ConfigTag.label, IPXON.PXONTag.label.toTag());
    KEY_MAP.put(ConfigTag.couplings, IPXON.PXONTag.couplings.toReadOnlyTag());

    IGNORED_PXON_TAGS.add(ConfigTag.code);
    IGNORED_PXON_TAGS.add(ConfigTag.type);
  }
  
  public ConfigEmbeddedRelationDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }

  @Override
  public ICSEElement toCSExpressID()
  {
    return null;
  }

  @Override
  void fromCSExpression (JSONContentParser parser) throws CSFormatException {
    //No CSE Element
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
  public void setType(String type)
  {
    setString(ConfigTag.type, type);
  }
  
  @Override
  public void setCode(String code)
  {
    setString(ConfigTag.code, code);
  }

  @Override
  public void setCouplings(Collection<IJSONContent> couplings)
  {
    configData.setField(ConfigTag.couplings.name(), couplings);
  }
}
