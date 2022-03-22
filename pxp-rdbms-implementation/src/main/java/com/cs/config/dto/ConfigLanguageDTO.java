package com.cs.config.dto;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.cs.config.idto.IConfigLanguageDTO;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;

public class ConfigLanguageDTO extends AbstractConfigJSONDTO implements IConfigLanguageDTO{
  
  private static final long                       serialVersionUID  = 1L;
  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();
  
  /** Initializing the static key map */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.statusTag, IPXON.PXONTag.statustag.toReadOnlyTag());
    
    // update only Properties
    KEY_MAP.put(ConfigTag.label, IPXON.PXONTag.label.toTag());
    KEY_MAP.put(ConfigTag.icon, IPXON.PXONTag.icon.toTag());
    KEY_MAP.put(ConfigTag.abbreviation, IPXON.PXONTag.abbreviation.toTag());
    KEY_MAP.put(ConfigTag.localeId, IPXON.PXONTag.localeId.toTag());
    KEY_MAP.put(ConfigTag.parentCode, IPXON.PXONTag.parentCode.toTag());
    KEY_MAP.put(ConfigTag.dateFormat, IPXON.PXONTag.dateFormat.toTag());
    KEY_MAP.put(ConfigTag.numberFormat, IPXON.PXONTag.numberFormat.toTag());
    KEY_MAP.put(ConfigTag.isDataLanguage, IPXON.PXONTag.isDataLanguage.toTag());
    KEY_MAP.put(ConfigTag.isDefaultLanguage, IPXON.PXONTag.isDefaultLanguage.toTag());
    KEY_MAP.put(ConfigTag.isUserInterfaceLanguage, IPXON.PXONTag.isUserInterfaceLanguage.toTag());
    KEY_MAP.put(ConfigTag.isStandard, IPXON.PXONTag.isStandard.toTag());
    
    IGNORED_PXON_TAGS.add(ConfigTag.code);
  }
  
  public ConfigLanguageDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }
  
  @Override
  public String getLabel()
  {
    return getString(ConfigTag.label);
  }

  @Override
  public String getIcon()
  {
    return getString(ConfigTag.icon);
  }

  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    CSEObject cse = new CSEObject(CSEObjectType.LanguageConf);
    String code = getString(ConfigTag.code);
    cse.setCode(code);
    return cse;
  }

  @Override
  void fromCSExpression (JSONContentParser parser) throws CSFormatException
  {
    fromCSExpressID( parser.getCSEElement(IPXON.PXONTag.csid.toTag()));
  }
  
  @Override
  public void setLabel(String label)
  {
    setString(ConfigTag.label, label);
  }

  @Override
  public void setIcon(String icon)
  {
    setString(ConfigTag.icon, icon);
  }

  @Override
  public void setCode(String code) 
  {
    setString(ConfigTag.code, code);
  }

  @Override
  public String getAbbreviation()
  {
    return getString(ConfigTag.abbreviation);
  }

  @Override
  public void setAbbreviation(String abbreviation)
  {
    setString(ConfigTag.abbreviation, abbreviation);
  }

  @Override
  public String getLocaleId()
  {
    return getString(ConfigTag.localeId);
  }
  
  @Override public String getParentCode()
  {
    return getString(ConfigTag.parentCode);
  }
  
  @Override
  public void setParentCode(String parentCode)
  {
    setString(ConfigTag.parentCode, parentCode);
  }

  @Override
  public void setLocaleId(String localeId)
  {
    setString(ConfigTag.localeId, localeId);
  }

  @Override
  public Boolean isDataLanguage()
  {
    return getBoolean(ConfigTag.isDataLanguage);
  }

  @Override
  public void setIsDataLanguage(Boolean isDataLanguage) {
    setBoolean(ConfigTag.isDataLanguage, isDataLanguage);
  }

  @Override
  public Boolean isDefaultLanguage()
  {
    return getBoolean(ConfigTag.isDefaultLanguage);
  }

  @Override
  public void setIsDefaultLanguage(Boolean isDefaultLanguage)
  {
    setBoolean(ConfigTag.isDefaultLanguage, isDefaultLanguage);
  }

  @Override
  public Boolean isUserInterfaceLanguage()
  {
    return getBoolean(ConfigTag.isUserInterfaceLanguage);
  }

  @Override
  public void setIsUserInterfaceLanguage(Boolean isUserInterfaceLanguage)
  {
    setBoolean(ConfigTag.isUserInterfaceLanguage, isUserInterfaceLanguage);
  }

  @Override
  public String getDateFormat()
  {
    return getString(ConfigTag.dateFormat);
  }

  @Override
  public void setDateFormat(String dateFormat)
  {
    setString(ConfigTag.dateFormat, dateFormat);
  }

  @Override
  public String getNumberFormat()
  {
    return getString(ConfigTag.numberFormat);
  }

  @Override
  public void setNumberFormat(String numberFormat)
  {
    setString(ConfigTag.numberFormat, numberFormat);
  }

  @Override
  public Boolean isStandard()
  {
    return getBoolean(ConfigTag.isStandard);
  }

  @Override
  public void setIsStandard(Boolean isStandard)
  {
    setBoolean(ConfigTag.isStandard, isStandard);
  }
}
