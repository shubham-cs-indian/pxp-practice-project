package com.cs.config.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.cs.config.idto.IConfigContextDTO;
import com.cs.config.standard.IConfigMap;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dto.ContextDTO;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.ijosn.IJSONContent;

public class ConfigContextDTO extends AbstractConfigJSONDTO implements IConfigContextDTO {
  
  private static final BidiMap<ConfigTag, String> KEY_MAP = new DualHashBidiMap<>();
  private static final Set<ConfigTag> IGNORED_PXON_TAGS = new HashSet<>();
  
  /** Initializing the static key map */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.type, IPXON.PXONTag.type.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.classCode, IPXON.PXONTag.classcode.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.isStandard, IPXON.PXONTag.isstandard.toReadOnlyTag());

    // update only Properties
    KEY_MAP.put(ConfigTag.defaultEndTime, IPXON.PXONTag.defaultendtime.toTag());
    KEY_MAP.put(ConfigTag.defaultStartTime, IPXON.PXONTag.defaultstarttime.toTag());
    KEY_MAP.put(ConfigTag.entities, IPXON.PXONTag.entities.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.isAutoCreate, IPXON.PXONTag.isautocreate.toTag());
    KEY_MAP.put(ConfigTag.isCurrentTime, IPXON.PXONTag.iscurrenttime.toTag());
    KEY_MAP.put(ConfigTag.isDuplicateVariantAllowed,
        IPXON.PXONTag.isduplicatevariantallowed.toTag());
    KEY_MAP.put(ConfigTag.isTimeEnabled, IPXON.PXONTag.istimeenabled.toTag());
    KEY_MAP.put(ConfigTag.label, IPXON.PXONTag.label.toTag());
    KEY_MAP.put(ConfigTag.tab, IPXON.PXONTag.tab.toTag());
    KEY_MAP.put(ConfigTag.tagCodes, IPXON.PXONTag.tagcodes.toJSONContentTag());
    KEY_MAP.put(ConfigTag.uniqueSelectors, IPXON.PXONTag.uniqueselectors.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.icon, IPXON.PXONTag.icon.toTag());

    IGNORED_PXON_TAGS.add(ConfigTag.code);
    IGNORED_PXON_TAGS.add(ConfigTag.type);
  }
  
  private final ContextDTO contextDTO = new ContextDTO();
  
  public ConfigContextDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }

  @Override
  public void fromJSON(String json) throws CSFormatException {
    super.fromJSON(json);
    initContextDTO();
  }

  private void initContextDTO() {
    contextDTO.setCode(getString(ConfigTag.code));
    contextDTO.setContextType(IConfigMap.getContextType(getString(ConfigTag.type)));
  }

  @Override
  public ICSEElement toCSExpressID() throws CSFormatException {
    return contextDTO.toCSExpressID();
  }

  @Override
  void fromCSExpression (JSONContentParser parser) throws CSFormatException {
    fromCSExpressID( parser.getCSEElement(IPXON.PXONTag.csid.toTag()));
  }

  /**
   * 1. Initialize data from string json 2. Parse PXON tags into normal tags 3.
   * Prepare contextDTO 4. Prepare csid related data into attribute JSON flat
   * keys
   */
  @Override
  public void fromPXON(String json) throws CSFormatException {
    super.fromPXON(json);
    contextDTO.fromPXON(json);
    String contextType = contextDTO.getContextType().toString();
    ContextType eContextType = IContextDTO.ContextType.valueOf(contextType);
    setString(ConfigTag.code, contextDTO.getCode());
    setString(ConfigTag.type, IConfigMap.getContextTypeClass(eContextType));
  }
  
  @Override
  public String getLabel()
  {
    return getString(ConfigTag.label);
  }
  
  
  @Override
  public boolean isAutoCreate()
  {
    return getBoolean(ConfigTag.isAutoCreate);
  }
  
  @Override
  public boolean isTimeEnabled()
  {
    return getBoolean(ConfigTag.isTimeEnabled);
  }
  
  @Override
  public long getDefaultStartTime()
  {
    return getLong(ConfigTag.defaultStartTime);
  }
  
  @Override
  public long getDefaultEndTime()
  {
    return getLong(ConfigTag.defaultEndTime);
  }
  
  @Override
  public boolean isCurrentTime()
  {
    return getBoolean(ConfigTag.isCurrentTime);
  }
  
  @Override
  public boolean isDuplicateVariantAllowed()
  {
    return getBoolean(ConfigTag.isDuplicateVariantAllowed);
  }
  
  @Override
  public String getTab()
  {
    return getString(ConfigTag.tab);
  }

  @Override
  public String getType()
  {
    return getString(ConfigTag.type);
  }

  @Override
  public IJSONContent getTagCodes()
  {
    return getJSONContent(ConfigTag.tagCodes);
  }
  
  @Override
  public Collection<String> getEntities()
  {
    return getJSONArray(ConfigTag.entities);
  }

  @Override
  public void setLabel(String label)
  {
    setString(ConfigTag.label, label);
  }


  @Override
  public void setIsAutoCreate(boolean isAutoCreate)
  {
    setBoolean(ConfigTag.isAutoCreate, isAutoCreate);
    
  }

  @Override
  public void setIsTimeEnabled(boolean isTimeEnabled)
  {
    setBoolean(ConfigTag.isTimeEnabled, isTimeEnabled);
  }

  @Override
  public void setDefaultStartTime(long defaultStartTime)
  {
    setLong(ConfigTag.defaultStartTime, defaultStartTime);
  }

  @Override
  public void setDefaultEndTime(long defaultEndTime)
  {
    setLong(ConfigTag.defaultEndTime, defaultEndTime);
  }

  @Override
  public void setIsCurrentTime(boolean isCurrentTime)
  {
    setBoolean(ConfigTag.isCurrentTime, isCurrentTime);
  }

  @Override
  public void setIsDuplicateVariantAllowed(boolean isDuplicateVariantAllowed)
  {
    setBoolean(ConfigTag.isDuplicateVariantAllowed, isDuplicateVariantAllowed);
  }

  @Override
  public void setTab(String tab)
  {
    setString(ConfigTag.tab, tab);
  }

  @Override
  public void setTagCodes(IJSONContent tagCodes)
  {
    setJSONContent(ConfigTag.tagCodes, tagCodes);
  }

  @Override
  public void setEntities(Collection<String> entities)
  {
    configData.setStringArrayField(ConfigTag.entities.toString(), entities);
  }

  @Override
  public IContextDTO getContextDTO()
  {
    return contextDTO;
  }

  @Override
  public void setContextDTO(String contextCode, ContextType type)
  {
    contextDTO.setCode(contextCode);    
    contextDTO.setContextType(type);
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
