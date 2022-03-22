package com.cs.config.dto;

import com.cs.config.idto.IConfigSectionElementDTO;
import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
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
public class ConfigSectionElementDTO extends AbstractConfigJSONDTO implements IConfigSectionElementDTO {

  /**
   * ConfigSectionElementDTO keys and its corresponding PXON key map
   */
  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();
  

  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.attributeVariantContextCode,
        IPXON.PXONTag.attributevariantcontextcode.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.couplingType, IPXON.PXONTag.couplingtype.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.isInherited, IPXON.PXONTag.isinherited.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.isCutOff, IPXON.PXONTag.iscutoff.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.isTranslatable, IPXON.PXONTag.istranslatable.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.type, IPXON.PXONTag.type.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.rendererType, IPXON.PXONTag.rendererType.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.attribute, IPXON.PXONTag.attribute.toReadOnlyTag());
    
    // Updatable Properties
    KEY_MAP.put(ConfigTag.defaultUnit, IPXON.PXONTag.defaultunit.toTag());
    KEY_MAP.put(ConfigTag.defaultValue, IPXON.PXONTag.defaultvalue.toTag());
    KEY_MAP.put(ConfigTag.valueAsHtml, IPXON.PXONTag.valueashtml.toTag());
    KEY_MAP.put(ConfigTag.isDisabled, IPXON.PXONTag.isdisabled.toTag());
    KEY_MAP.put(ConfigTag.isIdentifier, IPXON.PXONTag.isidentifier.toTag());
    KEY_MAP.put(ConfigTag.isMandatory, IPXON.PXONTag.ismandatory.toTag());
    KEY_MAP.put(ConfigTag.isShould, IPXON.PXONTag.isshould.toTag());
    KEY_MAP.put(ConfigTag.isSkipped, IPXON.PXONTag.isskipped.toTag());
    KEY_MAP.put(ConfigTag.isVersionable, IPXON.PXONTag.isversionable.toTag());
    KEY_MAP.put(ConfigTag.isMultiselect, IPXON.PXONTag.ismultiselect.toTag());
    KEY_MAP.put(ConfigTag.prefix, IPXON.PXONTag.prefix.toTag());
    KEY_MAP.put(ConfigTag.suffix, IPXON.PXONTag.suffix.toTag());
    KEY_MAP.put(ConfigTag.tagType, IPXON.PXONTag.tagtype.toTag());
    KEY_MAP.put(ConfigTag.selectedTagValues, IPXON.PXONTag.selectedtagvalues.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.precision, IPXON.PXONTag.precision.toTag());

    IGNORED_PXON_TAGS.remove(ConfigTag.type);
    IGNORED_PXON_TAGS.remove(ConfigTag.code);
  }

  public ConfigSectionElementDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }
  
  @Override
  public void fromJSON(String json) throws CSFormatException {
    super.fromJSON(json);
    IJSONContent attributeContent = getJSONContent(ConfigTag.attribute);
    configData.setField(ConfigTag.isTranslatable.toString(), attributeContent.getInitField(ConfigTag.isTranslatable.toString(), false));
    configData.setField(ConfigTag.rendererType.toString(), attributeContent.getInitField(ConfigTag.rendererType.toString(), ""));
  }
  
  @Override
  public ICSEElement toCSExpressID()
  {
    return null;
  }

  @Override
  void fromCSExpression (JSONContentParser parser) throws CSFormatException {
    //NO CSE element
  }

  @Override
  public String getType()
  {
    return getString(ConfigTag.type);
  }

  @Override
  public String getAttributeVariantContextCode() {
    return getString(ConfigTag.attributeVariantContextCode);
  }

  @Override
  public String getCouplingType()
  {
    return getString(ConfigTag.couplingType);
  }

  @Override
  public String getDefaultUnit()
  {
    return getString(ConfigTag.defaultUnit);
  }

  @Override
  public String getDefaultValue()
  {
    return getString(ConfigTag.defaultValue);
  }

  @Override
  public String getDefaultValueAsHTML()
  {
    return getString(ConfigTag.valueAsHtml);
  }

  @Override
  public boolean isCutOff()
  {
    return getBoolean(ConfigTag.isCutOff);
  }

  @Override
  public boolean isInherited()
  {
    return getBoolean(ConfigTag.isInherited);
  }

  @Override
  public boolean isDisabled()
  {
    return getBoolean(ConfigTag.isDisabled);
  }

  @Override
  public boolean isIdentifier()
  {
    return getBoolean(ConfigTag.isIdentifier);
  }

  @Override
  public boolean isMandatory()
  {
    return getBoolean(ConfigTag.isMandatory);
  }

  @Override
  public boolean isShould()
  {
    return getBoolean(ConfigTag.isShould);
  }

  @Override
  public boolean isSkipped()
  {
    return getBoolean(ConfigTag.isSkipped);
  }

  @Override
  public boolean isVersionable()
  {
    return getBoolean(ConfigTag.isVersionable);
  }

  @Override
  public boolean isTranslatable()
  {
    return getBoolean(ConfigTag.isTranslatable);
  }

  @Override
  public boolean isMultiSelect()
  {
    return getBoolean(ConfigTag.isMultiselect);
  }

  @Override
  public String getPrefix()
  {
    return getString(ConfigTag.prefix);
  }

  @Override
  public String getSuffix()
  {
    return getString(ConfigTag.suffix);
  }

  @Override
  public String getTagType()
  {
    return getString(ConfigTag.tagType);
  }

  @Override
  public Collection<String> getSelectedTagValues()
  {
    return getJSONArray(ConfigTag.selectedTagValues);
  }

  @Override
  public int getPrecision()
  {
    return getInt(ConfigTag.precision);
  }

  @Override
  public void setType(String type)
  {
    setString(ConfigTag.type, type);
  }

  @Override
  public void setAttributeVariantContextCode(String attributeVariantContextCode)
  {
    setString(ConfigTag.attributeVariantContextCode, attributeVariantContextCode);
  }

  @Override
  public void setCouplingType(String couplingType)
  {
    setString(ConfigTag.couplingType, couplingType);
  }

  @Override
  public void setDefaultUnit(String defaultUnit)
  {
    setString(ConfigTag.defaultUnit, defaultUnit);
  }

  @Override
  public void setDefaultValue(String defaultValue)
  {
    setString(ConfigTag.defaultValue, defaultValue);
  }

  @Override
  public void setDefaultValueAsHTML(String defaultValueAsHTML)
  {
    setString(ConfigTag.valueAsHtml, defaultValueAsHTML);
  }

  @Override
  public void setIsCutOff(boolean isCutOff)
  {
    setBoolean(ConfigTag.isCutOff, isCutOff);
  }

  @Override
  public void setIsInherited(boolean isInherited)
  {
    setBoolean(ConfigTag.isInherited, isInherited);
  }

  @Override
  public void setIsDisabled(boolean isDisabled)
  {
    setBoolean(ConfigTag.isDisabled, isDisabled);
  }

  @Override
  public void setISIdentifier(boolean isIdentifier)
  {
    setBoolean(ConfigTag.isIdentifier, isIdentifier);
  }

  @Override
  public void setIsMandatory(boolean isMandatory)
  {
    setBoolean(ConfigTag.isMandatory, isMandatory);
  }

  @Override
  public void setIsShould(boolean isShould)
  {
    setBoolean(ConfigTag.isShould, isShould);
  }

  @Override
  public void setIsSkipped(boolean isSkipped)
  {
    setBoolean(ConfigTag.isSkipped, isSkipped);
  }

  @Override
  public void setIsVersionable(boolean isVersionable)
  {
    setBoolean(ConfigTag.isVersionable, isVersionable);
  }

  @Override
  public void setIsTranslatable(boolean isTranslatable)
  {
    setBoolean(ConfigTag.isTranslatable, isTranslatable);
  }

  @Override
  public void setIsMultiSelect(boolean isMultiSelect)
  {
    setBoolean(ConfigTag.isMultiselect, isMultiSelect);
  }

  @Override
  public void setPrefix(String prefix)
  {
    setString(ConfigTag.prefix, prefix);
  }

  @Override
  public void setSuffix(String suffix)
  {
    setString(ConfigTag.suffix, suffix);
  }

  @Override
  public void setTagType(String tagType)
  {
    setString(ConfigTag.tagType, tagType);
  }

  @Override
  public void setPrecision(int precision)
  {
    setInt(ConfigTag.precision, precision);
  }
  
  @Override
  public void setCode(String code) {
    setString(ConfigTag.code, code);
  }
  
  @Override
  public String getCode() {
    return getString(ConfigTag.code);
  }
  
  @Override
  public void setSelectedTagValues(Collection<String> tagValueCodes)
  {
    configData.setField(ConfigTag.selectedTagValues.name(), tagValueCodes);
  }

  @Override
  public String getRendereType()
  {
    return getString(ConfigTag.rendererType);
  }
  
  @Override
  public void setRendereType(String type)
  {
    setString(ConfigTag.rendererType, type);
  }
}
