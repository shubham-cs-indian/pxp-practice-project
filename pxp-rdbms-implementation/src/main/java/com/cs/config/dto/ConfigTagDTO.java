package com.cs.config.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.cs.config.idto.IConfigTagDTO;
import com.cs.config.idto.IConfigTagValueDTO;
import com.cs.config.standard.IConfigClass;
import com.cs.config.standard.IConfigMap;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.technical.exception.CSFormatException;

/**
 * Tag DTO from the configuration realm - tagsGroup contains children -
 * tagsValue has no children
 *
 * @author tauseef
 */
public class ConfigTagDTO extends AbstractConfigPropertyDTO implements IConfigTagDTO {
  
  /**
   * Tag keys and its corresponding PXON key map
   */
  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();
  
  /** Definition of the tag and its PXON key map */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.isDisabled, IPXON.PXONTag.isdisabled.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.isStandard, IPXON.PXONTag.isstandard.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.propertyIID, IPXON.PXONTag.propertyiid.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.tagType, IPXON.PXONTag.tagtype.toPrivateTag());
    KEY_MAP.put(ConfigTag.type, IPXON.PXONTag.type.toReadOnlyTag());

    // Updatable Properties
    KEY_MAP.put(ConfigTag.availability, IPXON.PXONTag.availability.toTag());
    KEY_MAP.put(ConfigTag.color, IPXON.PXONTag.color.toTag());
    KEY_MAP.put(ConfigTag.defaultValue, IPXON.PXONTag.defaultvalue.toTag());
    KEY_MAP.put(ConfigTag.description, IPXON.PXONTag.description.toTag());
    KEY_MAP.put(ConfigTag.isFilterable, IPXON.PXONTag.isfilterable.toTag());
    KEY_MAP.put(ConfigTag.isGridEditable, IPXON.PXONTag.isgrideditable.toTag());
    KEY_MAP.put(ConfigTag.isMultiselect, IPXON.PXONTag.ismultiselect.toTag());
    KEY_MAP.put(ConfigTag.isVersionable, IPXON.PXONTag.isversionable.toTag());
    KEY_MAP.put(ConfigTag.label, IPXON.PXONTag.label.toTag());
    KEY_MAP.put(ConfigTag.linkedMasterTagId, IPXON.PXONTag.linkedmastertag.toTag());
    KEY_MAP.put(ConfigTag.tagValueSequence, IPXON.PXONTag.tagvaluesequence.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.tooltip, IPXON.PXONTag.tooltip.toTag());

    KEY_MAP.put(ConfigTag.children, IPXON.PXONTag.children.toCSEListTag());
    KEY_MAP.put(ConfigTag.icon, IPXON.PXONTag.icon.toTag());
    KEY_MAP.put(ConfigTag.imageResolution, IPXON.PXONTag.imageresolution.toTag());
    KEY_MAP.put(ConfigTag.imageExtension, IPXON.PXONTag.imageextension.toTag());
    
    IGNORED_PXON_TAGS.add(ConfigTag.code);
    IGNORED_PXON_TAGS.add(ConfigTag.propertyIID);
    IGNORED_PXON_TAGS.add(ConfigTag.type);
    IGNORED_PXON_TAGS.add(ConfigTag.children);
  }
  private final List<IConfigTagValueDTO> childrenDTOs = new ArrayList<>();
  
  /**
   * Initializing the static key map
   */
  public ConfigTagDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected void initPropertyDTO() throws CSFormatException
  {
    propertyDTO.setCode( getString(ConfigTag.code));
    propertyDTO.setIID( getLong(ConfigTag.propertyIID));
    String sPropertyType = getString(ConfigTag.type);
    IPropertyDTO.PropertyType propertyType = IConfigMap.getPropertyType(sPropertyType);
    propertyDTO.setPropertyType(propertyType);
    // Load children tags from ODB JSON into config objects
    childrenDTOs.clear();
    JSONArray tagValues = getJSONArray(ConfigTag.children);
    for (Object value : tagValues) {
      JSONObject tagValue = (JSONObject) value;
      ConfigTagValueDTO tagValueDTO = new ConfigTagValueDTO();
      tagValueDTO.fromJSON(tagValue.toString());
      childrenDTOs.add(tagValueDTO);
    }
  }

  /**
   * 1. Initialize data from string json 2. Parse PXON tags into normal tags 3.
   * Prepare PropertyDTO 4. Prepare csid related data into tag JSON flat keys
   */
  @Override
  public void fromPXON(String json) throws CSFormatException
  {
    super.fromPXON(json);
    propertyDTO.fromPXON(json);
    // PXON children structure is loaded in configData
    // -> needs to be converted into ODB format for consistency
    JSONArray children = getJSONArray(ConfigTag.children);
    JSONArray odbChildren = new JSONArray();
    for(Object tagValue: children) {
      ConfigTagValueDTO tagValueDTO = new ConfigTagValueDTO();
      tagValueDTO.fromPXON(tagValue.toString());
      childrenDTOs.add(tagValueDTO);
      odbChildren.add( (new JSONContent(tagValueDTO.toJSON())).toJSONObject());
    }
    setJSONArray(ConfigTag.children, odbChildren);
    setString(ConfigTag.type, IConfigClass.PropertyClass.TAG_TYPE.toString());
  }

  @Override
  protected JSONContent toConfigPXONContent() throws CSFormatException {
    JSONContent pxonOutput = super.toConfigPXONContent();
    JSONArray pxonChildren = new JSONArray();
    for (IConfigTagValueDTO tagValueDTO: childrenDTOs) {
      pxonChildren.add(((ConfigTagValueDTO)tagValueDTO).toConfigPXONContent().toJSONObject());
    }

    pxonOutput.setField(KEY_MAP.get(ConfigTag.children), pxonChildren);
    return pxonOutput;
  }

  @Override
  public String getType()
  {
    return propertyDTO.getPropertyType().name();
  }
  
  @Override
  public String getLabel()
  {
    return getString(ConfigTag.label);
  }
  
  @Override
  public String getDescription()
  {
    return getString(ConfigTag.description);
  }
  
  @Override
  public String getColor()
  {
    return getString(ConfigTag.color);
  }
  
  @Override
  public String getLinkedMasterTag()
  {
    return getString(ConfigTag.linkedMasterTagId);
  }
  
  @Override
  public String getDefaultValue()
  {
    return getString(ConfigTag.defaultValue);
  }
  
  @Override
  public boolean isMultiSelect()
  {
    return getBoolean(ConfigTag.isMultiselect);
  }
  
  @Override
  public String getToolTip()
  {
    return getString(ConfigTag.tooltip);
  }
  
  @Override
  public boolean isFilterable()
  {
    return getBoolean(ConfigTag.isFilterable);
  }
  
  @Override
  public Collection<String> getAvailability()
  {
    return getJSONArray(ConfigTag.availability);
  }
  
  @Override
  public boolean isGridEditable()
  {
    return getBoolean(ConfigTag.isGridEditable);
  }
  
  @Override
  public boolean isVersionable()
  {
    return getBoolean(ConfigTag.isVersionable);
  }

  @Override
  public Collection<Map> getTagValueSequence() {
    return getJSONArray(ConfigTag.tagValueSequence);
  }

  @Override
  public List<IConfigTagValueDTO> getChildren() {
    return childrenDTOs;
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
  public void setColor(String color)
  {
    setString(ConfigTag.color, color);
  }

  @Override
  public void setLinkedMasterTag(String linkedMasterTag)
  {
    setString(ConfigTag.linkedMasterTagId, linkedMasterTag);
  }

  @Override
  public void setDefaultValue(String defaultValue)
  {
    setString(ConfigTag.defaultValue, defaultValue);
  }

  @Override
  public void setIsMultiSelect(boolean isMultiSelect)
  {
    setBoolean(ConfigTag.isMultiselect, isMultiSelect);
  }

  @Override
  public void setToolTip(String toolTip)
  {
    setString(ConfigTag.tooltip, toolTip);
  }

  @Override
  public void setIsFilterable(boolean isFilterable)
  {
    setBoolean(ConfigTag.isFilterable, isFilterable);
  }

  @Override
  public void setAvailability(Collection<String> availability)
  {
    configData.setStringArrayField(ConfigTag.availability.toString(), availability);
  }

  @Override
  public void setIsGridEditable(boolean isGridEditable)
  {
    setBoolean(ConfigTag.isGridEditable, isGridEditable);
  }

  @Override
  public void setIsVersionable(boolean isVersionable)
  {
    setBoolean(ConfigTag.isVersionable, isVersionable);
  }

  @Override
  public void setPropertyDTO(String code, PropertyType type)
  {
    propertyDTO.setCode(code);
    propertyDTO.setPropertyType(type);
  }

  @Override
  public void setTagType(String tagType)
  {
    setString(ConfigTag.tagType, tagType);
  }
  @Override
  public String getTagType()
  {
    return getString(ConfigTag.tagType);
  }

  @Override
  public void setIsdisabled(boolean isdisabled)
  {
    setBoolean(ConfigTag.isDisabled, isdisabled);
  }
  
  @Override
  public boolean isDisabled()
  {
    return getBoolean(ConfigTag.isDisabled);
  }

  @Override
  public void setIsStandard(boolean isStandard)
  {
    setBoolean(ConfigTag.isStandard, isStandard);
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
  public boolean isStandard()
  {
    return getBoolean(ConfigTag.isStandard);
  }

  @Override
  public Integer getImageResolution()
  {
    return getInt(ConfigTag.imageResolution);
  }

  @Override
  public void setImageResolution(Integer imageResolution)
  {
    setInt(ConfigTag.imageResolution, imageResolution);
  }

  @Override
  public String getImageExtension()
  {
    return getString(ConfigTag.imageExtension);
  }

  @Override
  public void setImageExtension(String imageExtension)
  {
    setString(ConfigTag.imageExtension, imageExtension);
  }
}
