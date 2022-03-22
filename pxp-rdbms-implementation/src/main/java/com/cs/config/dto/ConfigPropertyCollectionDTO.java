package com.cs.config.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.cs.config.idto.IConfigPropertyCollectionDTO;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.ijosn.IJSONContent;

public class ConfigPropertyCollectionDTO extends AbstractConfigJSONDTO
    implements IConfigPropertyCollectionDTO {
  
  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();

  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.isStandard, IPXON.PXONTag.isstandard.toReadOnlyTag());

    // update only Properties
    KEY_MAP.put(ConfigTag.label, IPXON.PXONTag.label.toTag());
    KEY_MAP.put(ConfigTag.isForXRay, IPXON.PXONTag.isforxray.toTag());
    KEY_MAP.put(ConfigTag.isDefaultForXRay, IPXON.PXONTag.isdefaultforxray.toTag());
    KEY_MAP.put(ConfigTag.tab, IPXON.PXONTag.tab.toTag());
    KEY_MAP.put(ConfigTag.elements, IPXON.PXONTag.elements.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.icon, IPXON.PXONTag.icon.toTag());

    IGNORED_PXON_TAGS.add(ConfigTag.code);
  }
  
  /**
   * Initializing the static key map
   */
  public ConfigPropertyCollectionDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }
  
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException {
    CSEObject cse = new CSEObject(CSEObjectType.PropertyCollection);
    String code = getString(ConfigTag.code);
    cse.setCode(code);
    return cse;
  }

  @Override
  void fromCSExpression (JSONContentParser parser) throws CSFormatException {
    fromCSExpressID( parser.getCSEElement(IPXON.PXONTag.csid.toTag()));
  }

  @Override
  public String getLabel()
  {
    return getString(ConfigTag.label);
  }
  
  @Override
  public boolean isStandard()
  {
    return getBoolean(ConfigTag.isStandard);
  }
  
  @Override
  public boolean isForXRay()
  {
    return getBoolean(ConfigTag.isForXRay);
  }
  
  @Override
  public boolean isDefaultForXRay()
  {
    return getBoolean(ConfigTag.isDefaultForXRay);
  }
  
  @Override
  public String getTab()
  {
    return getString(ConfigTag.tab);
  }
  
  @Override
  public List getElements()
  {
    return getJSONArray(ConfigTag.elements);
  }

  @Override
  public void setLabel(String label)
  {
    setString(ConfigTag.label, label);
  }

  @Override
  public void setIsStandard(boolean isStandard)
  {
    setBoolean(ConfigTag.isStandard, isStandard);
  }

  @Override
  public void setIsForXRay(boolean isForXRay)
  {
    setBoolean(ConfigTag.isForXRay, isForXRay);
  }

  @Override
  public void setIsDefaultForXRay(boolean isDefaultForXRay)
  {
    setBoolean(ConfigTag.isDefaultForXRay, isDefaultForXRay);
  }

  @Override
  public void setTab(String tab)
  {
    setString(ConfigTag.tab, tab);
  }
  
  @Override
  public void setCode(String code) 
  {
    setString(ConfigTag.code, code);
  }

  @Override
  public void setElements(Collection<IJSONContent> elements)
  {
    configData.setField(ConfigTag.elements.toString(), elements);
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
}
