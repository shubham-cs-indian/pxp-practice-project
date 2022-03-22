package com.cs.config.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.cs.config.idto.IConfigAttributeDTO;
import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.standard.IConfigClass;
import com.cs.config.standard.IConfigMap;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * @author vallee
 */
public class ConfigAttributeDTO extends AbstractConfigPropertyDTO implements IConfigAttributeDTO {

  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();
  /** Definition of the attribute and its PXON key map */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.isDisabled, IPXON.PXONTag.isdisabled.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.isTranslatable, IPXON.PXONTag.istranslatable.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.propertyIID, IPXON.PXONTag.propertyiid.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.type, IPXON.PXONTag.type.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.subType, IPXON.PXONTag.subtype.toPrivateTag());
    KEY_MAP.put(ConfigTag.isStandard, IPXON.PXONTag.isstandard.toReadOnlyTag());
    
    // Updatable Properties
    KEY_MAP.put(ConfigTag.allowedStyles, IPXON.PXONTag.allowedstyles.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.availability, IPXON.PXONTag.availability.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.defaultValue, IPXON.PXONTag.defaultvalue.toTag());
    KEY_MAP.put(ConfigTag.defaultUnit, IPXON.PXONTag.defaultunit.toTag());
    KEY_MAP.put(ConfigTag.description, IPXON.PXONTag.description.toTag());
    KEY_MAP.put(ConfigTag.attributeConcatenatedList, IPXON.PXONTag.attributeconcatenatedlist.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.attributeOperatorList, IPXON.PXONTag.attributeoperatorlist.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.isFilterable, IPXON.PXONTag.isfilterable.toTag());
    KEY_MAP.put(ConfigTag.isGridEditable, IPXON.PXONTag.isgrideditable.toTag());
    KEY_MAP.put(ConfigTag.isMandatory, IPXON.PXONTag.ismandatory.toTag());
    KEY_MAP.put(ConfigTag.isSearchable, IPXON.PXONTag.issearchable.toTag());
    KEY_MAP.put(ConfigTag.isSortable, IPXON.PXONTag.issortable.toTag());
    KEY_MAP.put(ConfigTag.isVersionable, IPXON.PXONTag.isversionable.toTag());
    KEY_MAP.put(ConfigTag.label, IPXON.PXONTag.label.toTag());
    KEY_MAP.put(ConfigTag.precision, IPXON.PXONTag.precision.toTag());
    KEY_MAP.put(ConfigTag.placeholder, IPXON.PXONTag.placeholder.toTag());
    KEY_MAP.put(ConfigTag.tooltip, IPXON.PXONTag.tooltip.toTag());
    KEY_MAP.put(ConfigTag.valueAsHtml, IPXON.PXONTag.valueashtml.toTag());
    KEY_MAP.put(ConfigTag.icon, IPXON.PXONTag.icon.toTag());
    KEY_MAP.put(ConfigTag.calculatedAttributeUnit, IPXON.PXONTag.calcattributeunit.toTag());
    KEY_MAP.put(ConfigTag.calculatedAttributeUnitAsHTML, IPXON.PXONTag.calcattributeunitashtml.toTag());
    KEY_MAP.put(ConfigTag.calculatedAttributeType, IPXON.PXONTag.calculatedattributetype.toTag());
    KEY_MAP.put(ConfigTag.isCodeVisible, IPXON.PXONTag.iscodevisible.toTag());
    KEY_MAP.put(ConfigTag.hideSeparator, IPXON.PXONTag.hideseparator.toTag());
    
        
    IGNORED_PXON_TAGS.add(ConfigTag.code);
    IGNORED_PXON_TAGS.add(ConfigTag.propertyIID);
    IGNORED_PXON_TAGS.add(ConfigTag.type);
  }
  
  /**
   * Initializing the static key map
   */
  public ConfigAttributeDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }
  
  @Override
  protected void initPropertyDTO() throws CSFormatException
  {
    propertyDTO.setCode( getString(ConfigTag.code));
    propertyDTO.setIID( getLong(ConfigTag.propertyIID));
    String sPropertyType = getString(ConfigTag.type);
    // convert ODB class path into Property type
    IPropertyDTO.PropertyType propertyType = IConfigMap.getPropertyType(sPropertyType);
    String subType = IConfigClass.PropertyClass.valueOfClassPath(sPropertyType).name();
    setString(ConfigTag.subType, subType);
    propertyDTO.setPropertyType(propertyType);
  }
 
  /**
   * 1. Initialize data from string json 2. Parse PXON tags into normal tags 3.
   * Prepare PropertyDTO 4. Prepare csid related data into attribute JSON flat
   * keys
   */
  @Override
  public void fromPXON(String json) throws CSFormatException
  {
    super.fromPXON(json);
    propertyDTO.fromPXON(json);
    String subType = getString(ConfigTag.subType);
    IConfigClass.PropertyClass propertyType = IConfigClass.PropertyClass.valueOf(subType);
    setLong(ConfigTag.propertyIID, propertyDTO.getPropertyIID());
    setString(ConfigTag.code, propertyDTO.getCode());
    setString(ConfigTag.type, propertyType.toString());
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
  public String getDescription()
  {
    return getString(ConfigTag.description);
  }
  
  @Override
  public String getToolTip()
  {
    return getString(ConfigTag.tooltip);
  }
  
  @Override
  public String getPlaceHolder()
  {
    return getString(ConfigTag.placeholder);
  }
  
  @Override
  public String getDefaultUnit()
  {
    return getString(ConfigTag.defaultUnit);
  }
  
  @Override
  public boolean isMandatory()
  {
    return getBoolean(ConfigTag.isMandatory);
  }
  
  @Override
  public boolean isStandard()
  {
    return getBoolean(ConfigTag.isStandard);
  }
  
  @Override
  public boolean isDisabled()
  {
    return getBoolean(ConfigTag.isDisabled);
  }
  
  @Override
  public boolean isSearchable()
  {
    return getBoolean(ConfigTag.isSearchable);
  }
  
  @Override
  public boolean isGridEditable()
  {
    return getBoolean(ConfigTag.isGridEditable);
  }
  
  @Override
  public boolean isFilterable()
  {
    return getBoolean(ConfigTag.isFilterable);
  }
  
  @Override
  public boolean isSortable()
  {
    return getBoolean(ConfigTag.isSortable);
  }
  
  @Override
  public boolean isTranslatable()
  {
    return getBoolean(ConfigTag.isTranslatable);
  }
  
  @Override
  public boolean isVersionable()
  {
    return getBoolean(ConfigTag.isVersionable);
  }
  
  @Override
  public Collection<String> getAllowedStyles()
  {
    return getJSONArray(ConfigTag.allowedStyles);
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
  public Collection<String> getAvailability()
  {
    return getJSONArray(ConfigTag.availability);
  }
  
  @Override
  public List getAttributeConcatenatedList()
  {
    return getJSONArray(ConfigTag.attributeConcatenatedList);
  }
  
  @Override
  public int getPrecision()
  {
    return getInt(ConfigTag.precision);
  }

  public void setPropertyIID (long iid) {
    setLong(ConfigTag.propertyIID, iid);
    propertyDTO.setIID(iid);
  }

  @Override
  public void setLabel(String label)
  {
    setString(ConfigTag.label, label);
  }

  @Override
  public void setDescription(String description)
  {
    setString(ConfigTag.description, description);
  }

  @Override
  public void setToolTip(String toolTip)
  {
    setString(ConfigTag.tooltip, toolTip);
  }

  @Override
  public void setPlaceHolder(String placeHolder)
  {
    setString(ConfigTag.placeholder, placeHolder);
  }

  @Override
  public void setDefaultUnit(String defaultUnit)
  {
    setString(ConfigTag.defaultUnit, defaultUnit);
  }

  @Override
  public void setIsMandatory(boolean isMandatory)
  {
    setBoolean(ConfigTag.isMandatory, isMandatory);
  }

  @Override
  public void setIsStandard(boolean isStandard)
  {
    setBoolean(ConfigTag.isStandard, isStandard);
  }

  @Override
  public void setIsDisabled(boolean isDisabled)
  {
    setBoolean(ConfigTag.isDisabled, isDisabled);
  }

  @Override
  public void setIsSearchable(boolean isSearchable)
  {
    setBoolean(ConfigTag.isSearchable, isSearchable);
  }

  @Override
  public void setIsGridEditable(boolean isGridEditable)
  {
    setBoolean(ConfigTag.isGridEditable, isGridEditable);
  }

  @Override
  public void setIsFilterable(boolean isFilterable)
  {
    setBoolean(ConfigTag.isFilterable, isFilterable);
  }

  @Override
  public void setIsSortable(boolean isSortable)
  {
    setBoolean(ConfigTag.isSortable, isSortable);
  }

  @Override
  public void setIsTranslatable(boolean isTranslatable)
  {
    setBoolean(ConfigTag.isTranslatable, isTranslatable);
  }

  @Override
  public void SetIsVersionable(boolean isVersionable)
  {
    setBoolean(ConfigTag.isVersionable, isVersionable);
  }

  @Override
  public void setAllowedStyles(Collection<String> allowedStyles)
  {
    configData.setStringArrayField(ConfigTag.allowedStyles.toString(), allowedStyles);    
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
  public void setAvailability(Collection<String> availability)
  {
    configData.setStringArrayField(ConfigTag.availability.toString(), availability);    
  }

  @Override
  public void setAttributeConcatenatedList(List<IJSONContent> attributeConcatenatedList)
  {
    configData.setField(ConfigTag.attributeConcatenatedList.toString(), attributeConcatenatedList);
  }

  @Override
  public void setPrecision(int precision)
  {
    setInt(ConfigTag.precision, precision);
  }

  @Override
  public IPropertyDTO getPropertyDTO()
  {
    return propertyDTO;
  }

  @Override
  public void setPropertyDTO(String code, PropertyType type)
  {
    propertyDTO.setCode(code);
    propertyDTO.setPropertyType(type);
  }

  @Override
  public void setSubType(String subtype)
  {
    setString(ConfigTag.subType, subtype);
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
  public List getAttributeOperatorList()
  {
    return getJSONArray(ConfigTag.attributeOperatorList);
  }

  @Override
  public void setAttributeOperatorList(List<IJSONContent> attributeOperatorList)
  {
    configData.setField(ConfigTag.attributeOperatorList.toString(), attributeOperatorList);
  }
  
  @Override
  public String getCalculatedAttributeUnit()
  {
    return getString(ConfigTag.calculatedAttributeUnit);
  }

  @Override
  public void setCalculatedAttributeUnit(String calculatedAttributeUnit)
  {
    setString(ConfigTag.calculatedAttributeUnit, calculatedAttributeUnit);
  }

  @Override
  public String getCalculatedAttributeUnitAsHTML()
  {
    return getString(ConfigTag.calculatedAttributeUnitAsHTML);
  }

  @Override
  public void setCalculatedAttributeUnitAsHTML(String calculatedAttributeUnitAsHTML)
  {
    setString(ConfigTag.calculatedAttributeUnitAsHTML, calculatedAttributeUnitAsHTML);
  }

  @Override
  public String getCalculatedAttributeType()
  {
    return getString(ConfigTag.calculatedAttributeType);
  }

  @Override
  public void setCalculatedAttributeType(String calculatedAttributeType)
  {
    setString(ConfigTag.calculatedAttributeType, calculatedAttributeType);
  }

  @Override
  public boolean isCodeVisible()
  {
    return getBoolean(ConfigTag.isCodeVisible);
  }

  @Override
  public void setIsCodeVisible(boolean isCodeVisible)
  {
    setBoolean(ConfigTag.isCodeVisible, isCodeVisible);
  }

  @Override
  public boolean hideSeparator()
  {
    return getBoolean(ConfigTag.hideSeparator);
  }

  @Override
  public void setHideSeparator(boolean hideSeparator)
  {
    setBoolean(ConfigTag.hideSeparator, hideSeparator);
  }
}
